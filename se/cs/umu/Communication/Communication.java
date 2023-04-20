package se.cs.umu.Communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Communication extends UnicastRemoteObject implements CommunicationInterface{

    String message;
    boolean newMessage = false;

    protected Communication() throws RemoteException {
        super();
    }

    @Override
    public String receiveFromNode() {
        newMessage = false;
        return message;
    }

    @Override
    public void sendToNodes(String message) {
        this.message = message;
        newMessage = true;
    }

    public boolean newMessageExists() {
        return newMessage;
    }
}
