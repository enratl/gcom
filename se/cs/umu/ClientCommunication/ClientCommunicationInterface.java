package se.cs.umu.ClientCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientCommunicationInterface extends Remote {

    ArrayList<String> listGroups() throws RemoteException;
    ArrayList<String> getGroupMembers(String groupName) throws RemoteException;
    boolean joinGroup(String groupName) throws RemoteException;
    boolean leaveGroup(String groupName) throws RemoteException;
    boolean createGroup(String groupName) throws RemoteException;
    boolean sendMessageToGroup(String message, String groupName) throws RemoteException;
    public void deliverMessage(String message) throws RemoteException;
    public void debugInterceptDelivery(boolean intercept) throws RemoteException;
    public void debugReleaseIntercepted() throws RemoteException;
    public String debugGetVectorClocks() throws RemoteException;

    public void addObserver() throws RemoteException;
}
