package se.cs.umu.ClientCommunication;

import se.cs.umu.GCom.GCom;
import se.cs.umu.test.testClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientCommunication extends UnicastRemoteObject implements ClientCommunicationInterface {

    private final GCom gcom;
    private String username;
    private ClientCommunicationObserver observer;

    public ClientCommunication(GCom gcom) throws RemoteException {
        super();
        this.gcom = gcom;
    }

    @Override
    public ArrayList<String> listGroups() throws RemoteException {
        return gcom.listGroups();
    }

    @Override
    public boolean explicitJoin(String groupName, ArrayList<String> group, String orderingType) throws RemoteException{
        return gcom.explicitJoin(groupName, group, orderingType);
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
    public boolean createGroup(String groupName, String ordering) throws RemoteException {
        return gcom.createGroup(groupName, ordering);
    }

    @Override
    public boolean deleteGroup(String groupName) throws RemoteException {
        return gcom.deleteGroup(groupName);
    }

    @Override
    public ArrayList<String> getGroupMembers(String groupName) throws RemoteException {
        return gcom.getGlobalGroupMembers(groupName);
    }

    @Override
    public boolean sendMessageToGroup(String message, String groupName) throws RemoteException {
        return gcom.sendMessageToGroup(message, groupName);
    }

    @Override
    public void debugInterceptMessages(boolean shouldIntercept) throws RemoteException {
        gcom.interceptMessages(shouldIntercept);
    }

    public void deliverMessage(String message, String groupName, String sender, int clientClock) {
        try {
            observer.displayMessage(message, groupName, sender, clientClock);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void getOrderingBufferContents(String bufferContents) {
        try {
            observer.displayOrderingBuffer(bufferContents);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayDebugBufferContents(String bufferContents) {
        try {
            observer.displayDebugBuffer(bufferContents);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void debugReleaseAllIntercepted() throws RemoteException {
        gcom.releaseAllIntercepted();
    }

    @Override
    public void debugReleaseOldestIntercepted() throws RemoteException {
        gcom.releaseOldestIntercepted();
    }

    @Override
    public void debugReleaseNewestIntercepted() throws RemoteException {
        gcom.releaseNewestIntercepted();
    }

    @Override
    public String debugGetVectorClocks() throws RemoteException {
        return gcom.getVectorClocks();
    }

    @Override
    public String debugGetUndeliveredMessages() throws RemoteException {
        return gcom.getUndeliveredMessages();
    }

    @Override
    public String debugGetMessageStatistics() throws RemoteException {
        observer.displaySendStatistics(gcom.getSendStatistics());
        return gcom.getSendStatistics();
    }

    @Override
    public void debugDropMessage(int index) throws RemoteException{
        gcom.dropMessage(index);
    }

    @Override
    public void addObserver() throws RemoteException {
        try {
            observer = (ClientCommunicationObserver) Naming.lookup("rmi://localhost/Client");
        } catch (NotBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
