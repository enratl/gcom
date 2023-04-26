package se.cs.umu.GCom;

import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.ClientCommunication.ClientCommunicationInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GCom {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        GroupManagement gm = new GroupManagement(args[0]);

        ClientCommunicationInterface CCom = new ClientCommunication(gm);
        Registry registry = LocateRegistry.createRegistry(1100);
        Naming.rebind("//0.0.0.0/ClientCom", CCom);
        System.out.println("GCom started");
    }
}
