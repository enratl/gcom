package se.cs.umu.Communication;

import se.cs.umu.App.DebuggerController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NodeCommunication extends UnicastRemoteObject implements NodeCommunicationInterface {

    String message;

    private DebuggerController controller;

    public NodeCommunication()  throws RemoteException {
        super();
    }

    @Override
    public void receiveFromNode(String message) {
        controller.displayMessage(message);
        System.out.println(message);
    }

    public void sendToNodes(String message, ArrayList<String> group) {
        for (String member : group) {
            try {
                NodeCommunicationInterface node = (NodeCommunicationInterface) Naming.lookup("rmi://" + member);
                node.receiveFromNode(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Sent message: " + message + "\n");
        this.message = message;
    }
}
