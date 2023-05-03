package se.cs.umu.ClientCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCommunicationObserver extends Remote{
    void update(String message, String groupName, String sender, int clientClock) throws RemoteException;
}
