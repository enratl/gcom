package se.cs.umu.GCom;

import se.cs.umu.GroupMap.GroupMapInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class GroupManagement {
    private final GroupMapInterface groupMap;
    private final ConcurrentHashMap<String, HashSet<String>> groups;


    public GroupManagement(String groupMapAddress) throws MalformedURLException, NotBoundException, RemoteException {
        groupMap = (GroupMapInterface) Naming.lookup("rmi://" + groupMapAddress + "/GroupMap");
        groups = new ConcurrentHashMap<>();

    }

    public ArrayList<String> listGroups() {
        try {
            return groupMap.getGroups();
        } catch (RemoteException e) {
            return null;
        }
    }

    public ArrayList<String> getGlobalGroupMembers(String groupName) {
        try {
            return groupMap.getGroupMembers(groupName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public ArrayList<String> getLocalGroupMembers(String groupName) {
        if (!groups.containsKey(groupName)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(groups.get(groupName));
    }

    public void createLocalGroup(String groupName) {
        groups.put(groupName, new HashSet<>());
    }

    public void removeLocalGroup(String groupName){
        groups.remove(groupName);
    }

    public boolean addToGlobalGroupList(String groupName, String username, String address) {
        try {
            groupMap.addGroupMember(groupName, username, address);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean leaveGroup(String groupName, String username) {
        try {
            groupMap.removeFromGroup(groupName, username);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean createGroup(String groupName, String orderingType) {
        try {
            groupMap.createGroup(groupName, orderingType);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean deleteGroup(String groupName){
        try {
            groupMap.deleteGroup(groupName);
            return true;
        } catch (RemoteException e){
            return false;
        }
    }


    public String getGroupOrderingType(String groupName) {
        try {
            return groupMap.getGroupOrderingType(groupName);
        } catch (RemoteException e) {
            return "error " + e.getMessage();
        }
    }

    public ArrayList<String> getGroupAvailableOrderingTypes() {
        try {
            return groupMap.getGroupAvailableOrderingTypes();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void addToLocalGroup(String groupName, String member) {
        if (groups.containsKey(groupName)) {
            groups.get(groupName).add(member);
        }
    }

    public void removeFromLocalGroup(String groupName, String member) {
        if (groups.containsKey(groupName)) {
            groups.get(groupName).remove(member);
        }
    }
}
