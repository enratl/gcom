package se.cs.umu.GroupMap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GroupMapInterface extends Remote {
    ArrayList<String> getGroupMembers(String groupName) throws RemoteException;

    void addGroupMember(String groupName, String memberName, String adr);

    void updateAdr(String memberName, String adr);

    boolean createGroup(String groupName);

    boolean userExists(String userName);

    void removeFromGroup(String groupName, String userName);
}
