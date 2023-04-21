package se.cs.umu.ClientCommunication;

import se.cs.umu.Communication.NodeCommunication;
import se.cs.umu.Communication.NodeCommunicationInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientCommunication extends UnicastRemoteObject implements ClientCommunicationInterface {

    private NodeCommunicationInterface nodeCom;

    public ClientCommunication() throws RemoteException {
        super();
        nodeCom = null;
    }

    @Override
    public boolean startApplication(String userName) {
        if (nodeCom != null) {
            return false;
        }

        try {
            nodeCom = new NodeCommunication();
            Registry registry = LocateRegistry.createRegistry(1100);
            Naming.rebind("//0.0.0.0/" + userName, nodeCom);
        } catch (MalformedURLException | RemoteException e) {
            return false;
        }

        return true;
    }

    @Override
    public ArrayList<String> listGroups() throws RemoteException {
        return null;
    }

    @Override
    public boolean joinGroup(String groupName) throws RemoteException {
        return false;
    }

    @Override
    public boolean leaveGroup(String groupName) throws RemoteException {
        return false;
    }

    @Override
    public boolean createGroup(String groupName) throws RemoteException {
        return false;
    }

    @Override
    public boolean sendMessageToGroup(String message, String groupName) throws RemoteException {
        return false;
    }
}
