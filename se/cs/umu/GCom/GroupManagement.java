package se.cs.umu.GCom;

import se.cs.umu.GroupMap.GroupMapInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GroupManagement {
    private final GroupMapInterface groupMap;

    public GroupManagement(String groupMapAddress) throws MalformedURLException, NotBoundException, RemoteException {
        groupMap = (GroupMapInterface) Naming.lookup("rmi://" + groupMapAddress + "/GroupMap");
    }

    public ArrayList<String> listGroups(){
        try {
            return groupMap.getGroups();
        } catch (RemoteException e){
            return null;
        }
    }

    public ArrayList<String> getGroupMembers(String groupName){
        try{
            return groupMap.getGroupMembers(groupName);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean joinGroup(String groupName, String username, String address){
        try{
            groupMap.addGroupMember(groupName, username, address);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean leaveGroup(String groupName, String username){
        try{
            groupMap.removeFromGroup(groupName, username);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean createGroup(String groupName){
        try{
            groupMap.createGroup(groupName);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean updateAddress(String username, String address){
        try{
            groupMap.addAddress(username, address);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }
}
