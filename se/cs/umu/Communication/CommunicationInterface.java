package se.cs.umu.Communication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CommunicationInterface extends Remote {
    String receiveFromNode() throws RemoteException;
    void sendToNodes(String message) throws RemoteException;
}
