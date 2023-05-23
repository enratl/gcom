package se.cs.umu.Communication;

import se.cs.umu.App.DebuggerController;
import se.cs.umu.GCom.GCom;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeCommunication extends UnicastRemoteObject implements NodeCommunicationInterface {

    private final GCom gcom;
    private DebuggerController controller;

    public NodeCommunication(GCom gcom) throws RemoteException {
        super();
        this.gcom = gcom;
    }

    @Override
    public void receiveFromNode(String groupName, String sender, String message,
                                HashMap<String, Integer> messageVectorClock) throws RemoteException {
        gcom.receiveMessage(groupName, sender, message, messageVectorClock);

        //controller.displayMessage(message);
        //System.out.println(message);
    }

    @Override
    public HashMap<String, Integer> getVectorClock(String groupName) throws RemoteException {
        HashMap<String, Integer> vc = gcom.getVectorClock(groupName);
        System.out.println(vc);
        return vc;
    }

    public void sendToNodes(String message, String groupName, ArrayList<String> group, HashMap<String, Integer> vectorClock) {
        for (String member : group) {
            try {
                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                node.receiveFromNode(groupName, gcom.getUsername(), message, vectorClock);
                gcom.addSendStatistics(member.split("/")[1], true);
            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                // Message was not received
                gcom.addSendStatistics(member.split("/")[1], false);
                gcom.displaySendStatistics();
            }
        }
    }


    public HashMap<String, Integer> initializeVectorClock(String groupName, ArrayList<String> group) {
        HashMap<String, Integer> vectorClock = new HashMap<>();

        for (String member : group) {
            try {
                // ignore ourselves if we are already a member
                if (member.split("/")[1].equals(gcom.getUsername())) {
                    continue;
                }

                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                HashMap<String, Integer> memberVC = node.getVectorClock(groupName);

                if (memberVC == null) {
                    continue;
                }

                for (String clockName : memberVC.keySet()) {
                    if (vectorClock.containsKey(clockName)) {
                        vectorClock.put(clockName, Math.max(vectorClock.get(clockName), memberVC.get(clockName)));
                    } else {
                        vectorClock.put(clockName, memberVC.get(clockName));
                    }
                }


            } catch (MalformedURLException | NotBoundException | RemoteException ignore) {
            }
        }

        if (!vectorClock.containsKey(gcom.getUsername())) {
            vectorClock.put(gcom.getUsername(), 0);
        }

        System.out.println("vectorClock initialized: " + vectorClock);
        return vectorClock;
    }

    public void addToGroup(String groupName, String memberName, String adr) throws RemoteException {
        gcom.addToGroup(groupName, adr + '/' + memberName);
    }

    public void removeFromGroup(String groupName, String memberName, String adr) throws RemoteException {
        gcom.removeFromGroup(groupName, adr + '/' + memberName);
    }

    public boolean joinGroup(ArrayList<String> group, String groupName) {

        for (String member : group) {
            try {
                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                node.addToGroup(groupName, gcom.getUsername(), gcom.getAddress());

                // Add this member to our group.
                gcom.addToGroup(groupName, member);

            } catch (MalformedURLException | NotBoundException | RemoteException ignore) {
                // we were not added this nodes local group
            }
        }

        return true;
    }

    public void leaveGroup(ArrayList<String> group, String groupName) {
        for (String member : group) {
            try {
                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                node.removeFromGroup(groupName, gcom.getUsername(), gcom.getAddress());

            } catch (MalformedURLException | NotBoundException | RemoteException ignore) {
                // we were not able to leave this nodes local group
            }
        }
    }

    public void deleteGroupForMembers(ArrayList<String> group, String groupName) {
        for (String member : group){
            try {
                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                node.deleteGroup(groupName);
            } catch (NotBoundException | MalformedURLException | RemoteException e) {
                // local group info wasn't deleted for member
            }
        }
    }

    public void deleteGroup(String groupName) throws RemoteException {
        gcom.deleteLocalGroupInfo(groupName);
    }
}
