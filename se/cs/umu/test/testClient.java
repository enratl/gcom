package se.cs.umu.test;

import se.cs.umu.ClientCommunication.ClientCommunicationObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class testClient extends UnicastRemoteObject implements ClientCommunicationObserver {
    public testClient() throws RemoteException {
        super();
    }

    @Override
    public void displayMessage(String message, String groupName, String sender, int clientClock) throws RemoteException{
        System.out.println("Client received message: " + message + "\n");
    }

    @Override
    public void displayOrderingBuffer(String bufferContents) throws RemoteException {

    }
}
