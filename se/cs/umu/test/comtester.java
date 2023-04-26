package se.cs.umu.test;



import se.cs.umu.ClientCommunication.ClientCommunicationInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class comtester {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        ClientCommunicationInterface clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");

        System.out.println(clientCom.startApplication("ItsDennis"));
        System.out.println(clientCom.createGroup("test1"));
        System.out.println(clientCom.createGroup("test2"));
        System.out.println(clientCom.joinGroup("test2"));
        System.out.println(clientCom.createGroup("test3"));
        System.out.println(clientCom.listGroups());
        System.out.println(clientCom.getGroupMembers("test2"));
    }
}
