package se.cs.umu.ClientCommunication;

import se.cs.umu.Communication.NodeCommunication;
import se.cs.umu.Communication.NodeCommunicationInterface;
import se.cs.umu.GCom.GroupManagement;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientCommunication extends UnicastRemoteObject implements ClientCommunicationInterface {

    private final GroupManagement groupManagement;
    private NodeCommunicationInterface nodeCom;
    private String username;

    public ClientCommunication(GroupManagement groupManagement) throws RemoteException {
        super();
        this.groupManagement = groupManagement;
        nodeCom = null;
    }

    @Override
    public boolean startApplication(String username) {
        if (nodeCom != null) {
            return false;
        }

        this.username = username;

        try {
            nodeCom = new NodeCommunication();
            Registry registry = LocateRegistry.createRegistry(1101);
            Naming.rebind("//0.0.0.0/" + username, nodeCom);

            groupManagement.updateAddress(username, InetAddress.getLocalHost().getHostAddress());
            return true;
        } catch (RemoteException | MalformedURLException | UnknownHostException e) {
            return false;
        }
    }

    @Override
    public ArrayList<String> listGroups() throws RemoteException {
        return groupManagement.listGroups();
    }

    @Override
    public boolean joinGroup(String groupName) throws RemoteException {
        try {
            return groupManagement.joinGroup(groupName, username, InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e){
            return false;
        }
    }

    @Override
    public boolean leaveGroup(String groupName) throws RemoteException {
        return groupManagement.leaveGroup(groupName, username);
    }

    @Override
    public boolean createGroup(String groupName) throws RemoteException {
        return groupManagement.createGroup(groupName);
    }

    @Override
    public ArrayList<String> getGroupMembers(String groupName) throws RemoteException {
        return groupManagement.getGroupMembers(groupName);
    }

    @Override
    public boolean sendMessageToGroup(String message, String groupName) throws RemoteException {
        return false;
    }
}
