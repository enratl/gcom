package se.cs.umu.GroupMap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GroupMapInterface extends Remote {
    ArrayList<String> getGroupMembers(String groupName) throws RemoteException;

    void addGroupMember(String groupName, String memberName, String adr) throws RemoteException;



    boolean createGroup(String groupName, String ordering) throws RemoteException;



    void removeFromGroup(String groupName, String userName) throws RemoteException;

    String getGroupOrderingType(String groupName) throws RemoteException;
    ArrayList<String> getGroupAvailableOrderingTypes() throws RemoteException;

    ArrayList<String> getGroups() throws RemoteException;
}
