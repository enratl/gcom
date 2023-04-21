package se.cs.umu.Communication;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CommunicationServer {
    public static void main(String[] args) {
        try {
            NodeCommunicationInterface communication = new NodeCommunication();
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("Communication", communication);

            System.out.println("Server READY boi");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
