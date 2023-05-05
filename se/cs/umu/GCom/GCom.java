package se.cs.umu.GCom;

import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.Communication.NodeCommunication;
import se.cs.umu.MessageOrdering.CausalOrdering;
import se.cs.umu.MessageOrdering.MessageOrdering;
import se.cs.umu.MessageOrdering.UnorderedOrdering;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GCom {

    private final String username;
    private final String address;
    private final GroupManagement groupManagement;
    private final ClientCommunication clientCommunication;
    private final NodeCommunication nodeCommunication;
    private final ConcurrentHashMap<String, MessageOrdering> messageOrderings;
    private final Debugger debugger;

    public GCom(String username, String groupMapAddress) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        debugger = new Debugger();
        this.username = username;

        groupManagement = new GroupManagement(groupMapAddress);
        clientCommunication = new ClientCommunication(this);
        nodeCommunication = new NodeCommunication(this);
        messageOrderings = new ConcurrentHashMap<>();
        address = InetAddress.getLocalHost().getHostAddress();

        Registry clientComRegistry = LocateRegistry.createRegistry(1100);
        Naming.rebind("//0.0.0.0/ClientCom", clientCommunication);

        Registry nodeComRegistry = LocateRegistry.createRegistry(1101);
        Naming.rebind("//0.0.0.0/" + username, nodeCommunication);
    }

    public ArrayList<String> listGroups() {
        return groupManagement.listGroups();
    }

    public boolean explicitJoin(String groupName, ArrayList<String> group, String orderingType) {
        if (messageOrderings.containsKey(groupName)) {
            // we are already a member of the group.
            return true;
        }

        switch (orderingType) {
            case "CAUSAL" -> {
                HashMap<String, Integer> vectorClock = nodeCommunication.initializeVectorClock(groupName, group);
                messageOrderings.put(groupName, new CausalOrdering(vectorClock, groupName, this));
            }
            case "FIFO" -> {
                messageOrderings.put(groupName, new UnorderedOrdering(groupName, this));
            }
            default -> {
                // unsupported orderingType
                return false;
            }
        }

        groupManagement.createLocalGroup(groupName);
        nodeCommunication.joinGroup(group, groupName);

        return true;
    }

    public boolean joinGroup(String groupName) {
        if (messageOrderings.containsKey(groupName)) {
            // we are already a member of the group.
            return true;
        }

        String orderingType = groupManagement.getGroupOrderingType(groupName);

        switch (orderingType) {
            case "CAUSAL" -> {
                ArrayList<String> group = groupManagement.getGlobalGroupMembers(groupName);
                HashMap<String, Integer> vectorClock = nodeCommunication.initializeVectorClock(groupName, group);
                messageOrderings.put(groupName, new CausalOrdering(vectorClock, groupName, this));
            }
            case "FIFO" -> {
                messageOrderings.put(groupName, new UnorderedOrdering(groupName, this));
            }
            default -> {
                // groupMap might be down, explicit join could be needed
                return false;
            }
        }


        if (!groupManagement.addToGlobalGroupList(groupName, username, address)) {
            // Unable to add to global group list. (Might be down, explicit join could be needed)
            return false;
        }

        groupManagement.createLocalGroup(groupName);
        nodeCommunication.joinGroup(groupManagement.getGlobalGroupMembers(groupName), groupName);

        return true;
    }

    public boolean leaveGroup(String groupName) {
        if (!messageOrderings.containsKey(groupName)) {
            // we are not a member of the group.
            return true;
        }

        messageOrderings.remove(groupName);
        nodeCommunication.leaveGroup(groupManagement.getGlobalGroupMembers(groupName), groupName);
        groupManagement.removeLocalGroup(groupName);
        return groupManagement.leaveGroup(groupName, address + '/' + username);
    }

    public boolean createGroup(String groupName, String ordering) {
        return groupManagement.createGroup(groupName, ordering);
    }

    public ArrayList<String> getGlobalGroupMembers(String groupName) {
        System.out.println(groupManagement.getLocalGroupMembers(groupName));
        return groupManagement.getGlobalGroupMembers(groupName);
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public boolean sendMessageToGroup(String message, String groupName) {
        if (!messageOrderings.containsKey(groupName)) {
            return false;
        }

        ArrayList<String> group = groupManagement.getLocalGroupMembers(groupName);
        HashMap<String, Integer> vectorClock = messageOrderings.get(groupName).getVectorClock();
        vectorClock.replace(username, vectorClock.getOrDefault(username, 0) + 1);
        nodeCommunication.sendToNodes(message, groupName, group, vectorClock);
        displayDebugBufferContents();
        return true;
    }

    public void receiveMessage(String groupName, String sender, String message, HashMap<String, Integer> messageVectorClock) {
        System.out.println("GCom received message: " + message);

        if (debugger.shouldIntercept()) {
            debugger.intercept(groupName, sender, message, messageVectorClock);
        } else {
            messageOrderings.get(groupName).receiveMessage(sender, message, messageVectorClock);
        }
    }

    public void deliverMessage(String message, String groupName, String sender, int clientClock) {
        System.out.println("GCom delivered [message: " + message + ", groupName: " + groupName + ", sender: " + sender
                + ", clientClock: " + clientClock + "]");
        clientCommunication.deliverMessage(message, groupName, sender, clientClock);
    }

    public void displayOrderingBufferContents(String bufferContents) {
        clientCommunication.getOrderingBufferContents(bufferContents);
    }

    public HashMap<String, Integer> getVectorClock(String groupName) {
        return messageOrderings.get(groupName).getVectorClock();
    }

    public void interceptMessages(boolean shouldIntercept) {
        debugger.setShouldIntercept(shouldIntercept);
    }

    public void releaseAllIntercepted() {
        for (DebugBufferEntry e : debugger.releaseAllIntercepted()) {
            messageOrderings.get(e.groupName()).receiveMessage(e.sender(), e.message(), e.messageVectorClock());
        }

        displayDebugBufferContents();
    }

    public void releaseOldestIntercepted() {
        DebugBufferEntry e = debugger.releaseOldestIntercepted();

        if (e != null) {
            messageOrderings.get(e.groupName()).receiveMessage(e.sender(), e.message(), e.messageVectorClock());
        }
        displayDebugBufferContents();
    }

    public void releaseNewestIntercepted() {
        DebugBufferEntry e = debugger.releaseNewestIntercepted();

        if (e != null) {
            messageOrderings.get(e.groupName()).receiveMessage(e.sender(), e.message(), e.messageVectorClock());
        }
        displayDebugBufferContents();
    }

    public String getUndeliveredMessages() {
        StringBuilder sb = new StringBuilder();

        for (String group : messageOrderings.keySet()) {
            sb.append(group).append("\n");
            sb.append(messageOrderings.get(group).getBufferedMessages());
        }

        return sb.toString();
    }

    public void displayDebugBufferContents() {
        StringBuilder sb = new StringBuilder();

        for (DebugBufferEntry entry : debugger.getDebugBuffer()) {
            sb.append("{[" + entry.groupName() + "],["
                    + entry.sender() + "],["
                    + entry.message() + "],["
                    + entry.messageVectorClock().toString() + "]}\n");
        }

        clientCommunication.displayDebugBufferContents(sb.toString());
    }

    public String getVectorClocks() {
        StringBuilder sb = new StringBuilder();

        for (String group : messageOrderings.keySet()) {
            sb.append(group).append(" ").append(messageOrderings.get(group).getVectorClock());
        }

        return sb.toString();
    }

    public void addToGroup(String groupName, String member) {
        groupManagement.addToLocalGroup(groupName, member);
    }

    public void removeFromGroup(String groupName, String member) {
        groupManagement.removeFromLocalGroup(groupName, member);
    }

    public String getSendStatistics(){
        return debugger.getSendStatistics();
    }

    public void addSendStatistics(String receiver, boolean wasReceived){
        debugger.addSendStatistics(receiver, wasReceived);
    }
}
