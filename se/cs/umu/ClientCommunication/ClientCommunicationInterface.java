package se.cs.umu.ClientCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientCommunicationInterface extends Remote {

    boolean startApplication(String userName);
    ArrayList<String> listGroups() throws RemoteException;
    boolean joinGroup(String groupName) throws RemoteException;
    boolean leaveGroup(String groupName) throws RemoteException;
    boolean createGroup(String groupName) throws RemoteException;
    boolean sendMessageToGroup(String message, String groupName) throws RemoteException;
}
