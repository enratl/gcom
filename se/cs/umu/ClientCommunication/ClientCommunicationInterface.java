package se.cs.umu.ClientCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientCommunicationInterface extends Remote {

    ArrayList<String> listGroups() throws RemoteException;
    ArrayList<String> getGroupMembers(String groupName) throws RemoteException;
    boolean joinGroup(String groupName) throws RemoteException;
    boolean leaveGroup(String groupName) throws RemoteException;
    boolean createGroup(String groupName, String ordering) throws RemoteException;
    boolean sendMessageToGroup(String message, String groupName) throws RemoteException;
    void debugInterceptMessages(boolean intercept) throws RemoteException;
    void debugReleaseAllIntercepted() throws RemoteException;
    void debugReleaseOldestIntercepted() throws RemoteException;
    void debugReleaseNewestIntercepted() throws RemoteException;
    String debugGetVectorClocks() throws RemoteException;
    String debugGetUndeliveredMessages() throws RemoteException;
    void deliverMessage(String message, String groupName, String sender, int clientClock) throws RemoteException;
    void addObserver() throws RemoteException;
}
