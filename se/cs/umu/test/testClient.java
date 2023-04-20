package se.cs.umu.test;

import se.cs.umu.Communication.CommunicationInterface;
import se.cs.umu.Communication.Communicator;
import se.cs.umu.GroupMap.GroupMapInterface;

import javax.naming.Name;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class testClient {
    public static void main(String[] args) {
        try {
            //GroupMapInterface groupMap = (GroupMapInterface) Naming.lookup("rmi://localhost/GroupMap");

            //ArrayList<String> list = groupMap.hello();
            //System.out.println(list);

            CommunicationInterface communication = (CommunicationInterface) Naming.lookup("rmi://localhost/Communication");
            Communicator communicator = new Communicator(communication);

            ArrayList<CommunicationInterface> communicators = new ArrayList<>();
            communicators.add(communication);

            communicator.send(communicators, "Ding dong");
            String response = communicator.receive();

            System.out.println("Received message: " + response);

        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
