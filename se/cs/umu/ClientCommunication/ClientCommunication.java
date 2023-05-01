package se.cs.umu.ClientCommunication;

import se.cs.umu.GCom.GCom;
import se.cs.umu.test.testClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientCommunication extends UnicastRemoteObject implements ClientCommunicationInterface {

    private final GCom gcom;
    private String username;
    private ClientCommunicationObserver observer;

    public ClientCommunication(GCom gcom) throws RemoteException {
        super();
        this.gcom = gcom;
        observer = new testClient();
    }

    @Override
    public ArrayList<String> listGroups() throws RemoteException {
        return gcom.listGroups();
    }

    @Override
    public boolean joinGroup(String groupName) throws RemoteException {
        return gcom.joinGroup(groupName);
    }

    @Override
    public boolean leaveGroup(String groupName) throws RemoteException {
        return gcom.leaveGroup(groupName);
    }

    @Override
    public boolean createGroup(String groupName) throws RemoteException {
        return gcom.createGroup(groupName);
    }

    @Override
    public ArrayList<String> getGroupMembers(String groupName) throws RemoteException {
        return gcom.getGroupMembers(groupName);
    }

    @Override
    public boolean sendMessageToGroup(String message, String groupName) throws RemoteException {
        return gcom.sendMessageToGroup(message, groupName);
    }

    @Override
    public void debugInterceptMessages(boolean shouldIntercept) throws RemoteException{
        gcom.interceptMessages(shouldIntercept);
    }
    public void deliverMessage (String message) {
        try {
            observer.update(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void debugReleaseAllIntercepted() throws RemoteException{
        gcom.releaseAllIntercepted();
    }

    @Override
    public  void debugReleaseOldestIntercepted() throws RemoteException{
        gcom.releaseOldestIntercepted();
    }

    @Override
    public void debugReleaseNewestIntercepted() throws RemoteException{
        gcom.releaseNewestIntercepted();
    }

    @Override
    public String debugGetVectorClocks() throws RemoteException{
        return gcom.getVectorClocks();
    }

    @Override
    public String debugGetUndeliveredMessages() throws RemoteException{
        return gcom.getUndeliveredMessages();
    }

    @Override
    public void addObserver() throws RemoteException {
        try {
            observer = (ClientCommunicationObserver) Naming.lookup("rmi://localhost/testClient");
        } catch (NotBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
