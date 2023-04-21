package se.cs.umu.GCom;

import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.ClientCommunication.ClientCommunicationInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GCom {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        ClientCommunicationInterface CCom = new ClientCommunication();
        Registry registry = LocateRegistry.createRegistry(1099);
        Naming.rebind("//0.0.0.0/ClientCom", CCom);
    }
}
