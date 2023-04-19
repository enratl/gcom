package se.cs.umu.GroupMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class GroupMap extends UnicastRemoteObject implements GroupMapInterface {

    private final ConcurrentHashMap<String, HashSet<String>> groups;
    private final ConcurrentHashMap<String, String> addresses;

    public GroupMap() throws RemoteException {
        groups = new ConcurrentHashMap<>();
        addresses = new ConcurrentHashMap<>();
    }

    @Override
    public ArrayList<String> getGroupMembers(String groupName) throws RemoteException {
        ArrayList<String> members = new ArrayList<>();

        for (String member : groups.get(groupName)) {
            members.add(member + '/' + addresses.get(member));
        }

        return members;
    }

    @Override
    public void addGroupMember(String groupName, String memberName, String adr) {
        groups.get(groupName).add(memberName);
        addresses.put(memberName, adr);
    }

    @Override
    public void updateAdr(String memberName, String adr) {
        addresses.replace(memberName, adr);
    }

    @Override
    public boolean createGroup(String groupName) {
        if (groups.containsKey(groupName)) {
            return false;
        }

        groups.put(groupName, new HashSet<>());
        return true;
    }

    @Override
    public boolean userExists(String userName) {
        return addresses.containsKey(userName);
    }

    @Override
    public void removeFromGroup(String groupName, String userName) {
        groups.get(groupName).remove(userName);
    }


}
