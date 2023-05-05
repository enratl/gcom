package se.cs.umu.Communication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface NodeCommunicationInterface extends Remote {
    void receiveFromNode(String groupName, String sender, String message, HashMap<String, Integer> messageVectorClock) throws RemoteException;
    HashMap<String, Integer> getVectorClock(String groupName) throws RemoteException;
    void addToGroup (String groupName, String memberName, String adr) throws RemoteException;
    void removeFromGroup(String groupName, String memberName, String adr) throws RemoteException;
    void deleteGroup(String groupName) throws RemoteException;
}
