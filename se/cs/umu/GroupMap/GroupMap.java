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
            members.add(addresses.get(member) + '/' + member);
        }

        return members;
    }

    @Override
    public void addGroupMember(String groupName, String memberName, String adr) throws RemoteException {
        groups.get(groupName).add(memberName);
        addresses.put(memberName, adr);
    }

    @Override
    public void updateAdr(String memberName, String adr) throws RemoteException {
        addresses.replace(memberName, adr);
    }

    @Override
    public boolean createGroup(String groupName) throws RemoteException {
        if (groups.containsKey(groupName)) {
            return false;
        }

        groups.put(groupName, new HashSet<>());
        return true;
    }

    @Override
    public boolean userExists(String userName) throws RemoteException {
        return addresses.containsKey(userName);
    }

    @Override
    public void removeFromGroup(String groupName, String userName) throws RemoteException {
        groups.get(groupName).remove(userName);
    }
}
