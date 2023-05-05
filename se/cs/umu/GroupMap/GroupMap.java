package se.cs.umu.GroupMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class GroupMap extends UnicastRemoteObject implements GroupMapInterface {

    private final ConcurrentHashMap<String, HashSet<String>> groups;

    private final ConcurrentHashMap<String, String> orderingType;
    private final HashSet<String> availableOrderingTypes;

    public GroupMap() throws RemoteException {
        groups = new ConcurrentHashMap<>();
        availableOrderingTypes = new HashSet<>();
        orderingType = new ConcurrentHashMap<>();
        availableOrderingTypes.add("FIFO");
        availableOrderingTypes.add("CAUSAL");
    }

    @Override
    public ArrayList<String> getGroupMembers(String groupName) throws RemoteException {

        return new ArrayList<>(groups.get(groupName));
    }

    @Override
    public void addGroupMember(String groupName, String memberName, String adr) throws RemoteException {
        groups.get(groupName).add(adr + '/' + memberName);
    }

    @Override
    public boolean createGroup(String groupName, String ordering) throws RemoteException {
        if (groups.containsKey(groupName) || (!availableOrderingTypes.contains(ordering))) {
            return false;
        }

        groups.put(groupName, new HashSet<>());
        orderingType.put(groupName, ordering);
        return true;
    }


    @Override
    public void removeFromGroup(String groupName, String member) throws RemoteException {
        groups.get(groupName).remove(member);
    }

    @Override
    public ArrayList<String> getGroups() throws RemoteException{
        return new ArrayList<>(groups.keySet());
    }

    @Override
    public String getGroupOrderingType(String groupName) throws RemoteException{
        return orderingType.get(groupName);
    }

    @Override
    public ArrayList<String> getGroupAvailableOrderingTypes() throws RemoteException {
        return new ArrayList<>(availableOrderingTypes);
    }
}
