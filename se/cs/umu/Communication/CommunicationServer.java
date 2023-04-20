package se.cs.umu.Communication;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CommunicationServer {
    public static void main(String[] args) {
        try {
            CommunicationInterface communication = new Communication();
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//0.0.0.0/Communication", communication);

            System.out.println("Server READY boi");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
