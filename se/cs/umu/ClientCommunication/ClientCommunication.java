package se.cs.umu.ClientCommunication;

import se.cs.umu.GCom.GCom;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientCommunication extends UnicastRemoteObject implements ClientCommunicationInterface {

    private final GCom gcom;
    private String username;

    public ClientCommunication(GCom gcom) throws RemoteException {
        super();
        this.gcom = gcom;
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
}
