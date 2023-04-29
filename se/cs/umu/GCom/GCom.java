package se.cs.umu.GCom;

import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.Communication.NodeCommunication;
import se.cs.umu.MessageOrdering.CausalOrdering;
import se.cs.umu.MessageOrdering.MessageOrdering;

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

public class GCom {

    private final String username;
    private final GroupManagement groupManagement;
    private final ClientCommunication clientCommunication;
    private final NodeCommunication nodeCommunication;
    private final HashMap<String, MessageOrdering> messageOrderings;

    public GCom(String username, String groupMapAddress) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        this.username = username;

        groupManagement = new GroupManagement(groupMapAddress);
        groupManagement.updateAddress(username, InetAddress.getLocalHost().getHostAddress());
        clientCommunication = new ClientCommunication(this);
        nodeCommunication = new NodeCommunication(this);
        messageOrderings = new HashMap<>();


        Registry clientComRegistry = LocateRegistry.createRegistry(1100);
        Naming.rebind("//0.0.0.0/ClientCom", clientCommunication);

        Registry nodeComRegistry = LocateRegistry.createRegistry(1101);
        Naming.rebind("//0.0.0.0/" + username, nodeCommunication);
    }

    public ArrayList<String> listGroups() {
        return groupManagement.listGroups();
    }

    public boolean joinGroup(String groupName) {
        if (messageOrderings.containsKey(groupName)){
            return true;
        }

        try {
            ArrayList<String> group = groupManagement.getGroupMembers(groupName);
            HashMap<String, Integer> vectorClock = nodeCommunication.initializeVectorClock(groupName, group);
            messageOrderings.put(groupName, new CausalOrdering(vectorClock, groupName,this));
            System.out.println(vectorClock);
            return groupManagement.joinGroup(groupName, username, InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public boolean leaveGroup(String groupName) {
        return groupManagement.leaveGroup(groupName, username);
    }

    public boolean createGroup(String groupName) {
        return groupManagement.createGroup(groupName);
    }

    public ArrayList<String> getGroupMembers(String groupName) {
        return groupManagement.getGroupMembers(groupName);
    }

    public String getUsername() {
        return username;
    }

    public boolean sendMessageToGroup(String message, String groupName){
        if (!messageOrderings.containsKey(groupName)){
            return false;
        }

        ArrayList<String> group = groupManagement.getGroupMembers(groupName);
        HashMap<String, Integer> vectorClock = messageOrderings.get(groupName).getVectorClock();
        vectorClock.put(username, vectorClock.get(username) + 1);
        nodeCommunication.sendToNodes(message, groupName, group, vectorClock);
        return true;
    }

    public void receiveMessage(String groupName, String sender, String message,HashMap<String, Integer> messageVectorClock){
        System.out.println("GCom received message: " + message);
        messageOrderings.get(groupName).receiveMessage(sender, message, messageVectorClock);
    }

    public void deliverMessage(String message, String groupName, String sender, int clientClock){
        // deliver to client
        System.out.println("GCom delivered [message: " + message + ", groupName: " + groupName + ", sender: " + sender
                + ", clientClock: " + clientClock + "]");
    }

    public HashMap<String, Integer> getVectorClock(String groupName){
        return messageOrderings.get(groupName).getVectorClock();
    }
}
