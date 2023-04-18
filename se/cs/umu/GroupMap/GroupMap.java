package se.cs.umu.GroupMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GroupMap extends UnicastRemoteObject implements GroupMapInterface {

    ArrayList<String> list;
    public GroupMap() throws RemoteException{
        list = new ArrayList<>();
    }

    @Override
    public ArrayList<String> hello() throws RemoteException {
        list.add("pizza");
        list.add("nibbles");

        return list;
    }
}
