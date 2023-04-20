package se.cs.umu.Communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Communicator {

    CommunicationInterface communication;

    public Communicator(CommunicationInterface communication) {
        this.communication = communication;
    }
    public String receive() throws RemoteException {
        return communication.receiveFromNode();
    }

    public boolean send(ArrayList<CommunicationInterface> nodes, String message) throws RemoteException {
        for (CommunicationInterface node : nodes) {
            node.sendToNodes(message);
        }
        System.out.println("Sent message : " + message);
        return true;
    }
}
