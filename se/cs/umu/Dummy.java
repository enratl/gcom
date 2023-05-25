package se.cs.umu;

import se.cs.umu.App.DebuggerController;
import se.cs.umu.App.DebuggerGUI;
import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.ClientCommunication.ClientCommunicationInterface;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;
import se.cs.umu.GCom.GCom;
import se.cs.umu.GroupMap.GroupMap;
import se.cs.umu.GroupMap.GroupMapInterface;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Dummy {
    public static void main(String[] args) {
        try {

            GroupMapInterface groupMap = new GroupMap();
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//0.0.0.0/Dummy", groupMap);

            /*
            Enumeration<NetworkInterface> netInt = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface n : Collections.list(netInt)) {
                System.out.println("Display Name: " + n.getDisplayName());
                Enumeration<InetAddress> address = n.getInetAddresses();
                for (InetAddress a : Collections.list(address)) {
                    System.out.println("Address: " + a);
                }
            }
             */

            System.out.println("Port 1099 open");

        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
