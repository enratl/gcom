package se.cs.umu.test;

import se.cs.umu.GroupMap.GroupMapInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class testClient {
    public static void main(String[] args) {
        try {
            GroupMapInterface groupMap = (GroupMapInterface) Naming.lookup("rmi://localhost/GroupMap");

            ArrayList<String> list = groupMap.hello();
            System.out.println(list);

        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
