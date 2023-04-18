package se.cs.umu.GroupMap;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GroupMapServer {
    public static void main(String[] args){
        try{
            GroupMapInterface groupMap = new GroupMap();
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("GroupMap", groupMap);
            System.out.println("Server READY boi");

        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
