package se.cs.umu.GCom;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException, UnknownHostException {
        if (args.length < 2){
            System.out.println("GCom needs to be initialized with: 1. username, 2. GroupManagement ip address");
        }

        String username = args[0];
        String groupMapAddress = args[1];

        GCom GCom = new GCom(username, groupMapAddress);
    }
}
