package se.cs.umu.Communication;

import se.cs.umu.App.DebuggerController;
import se.cs.umu.GCom.GCom;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
        System.out.println("Getting vector clock");
        HashMap<String,Integer> vc = gcom.getVectorClock(groupName);
        System.out.println(vc);
        return vc;
    }

    public void sendToNodes(String message, String groupName, ArrayList<String> group, HashMap<String, Integer> vectorClock) {
        for (String member : group) {
            try {
                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                node.receiveFromNode(groupName, gcom.getUsername(), message, vectorClock);
            } catch (RemoteException | NotBoundException | MalformedURLException ignore) {
                // Message was not received
            }
        }
        System.out.printf("Sent message: " + message + "\n");
    }


    public HashMap<String, Integer> initializeVectorClock(String groupName, ArrayList<String> group) {
        HashMap<String, Integer> vectorClock = new HashMap<>();

        for (String member : group) {
            try {
                if (member.split("/")[1].equals(gcom.getUsername())){
                    continue;
                }

                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                HashMap<String, Integer> memberVC = node.getVectorClock(groupName);

                if (memberVC == null){
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
}
