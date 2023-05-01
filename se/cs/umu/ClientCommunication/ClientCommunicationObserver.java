package se.cs.umu.ClientCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCommunicationObserver extends Remote {
    public void update(String message) throws RemoteException;
}
