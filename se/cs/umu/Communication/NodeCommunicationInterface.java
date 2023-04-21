package se.cs.umu.Communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeCommunicationInterface extends Remote {
    void receiveFromNode(String message) throws RemoteException;
}
