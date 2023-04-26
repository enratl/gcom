package se.cs.umu.GroupMap;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GroupMapServer {
    public static void main(String[] args) {
        try {
            GroupMapInterface groupMap = new GroupMap();
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//0.0.0.0/GroupMap", groupMap);

            System.out.println("Group map server running on: " + InetAddress.getLocalHost().getHostAddress());

        } catch (RemoteException | MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
