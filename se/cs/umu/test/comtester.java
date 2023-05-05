package se.cs.umu.test;



import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.ClientCommunication.ClientCommunicationInterface;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Locale;
import java.util.Scanner;

public class comtester {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        ClientCommunicationInterface clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");

        Scanner scanner = new Scanner(System.in);

        ClientCommunicationObserver client;

        Registry clientRegistry = LocateRegistry.createRegistry(1103);
        //Naming.rebind("//0.0.0.0/testClient", client);
        //clientCom.addObserver();

        String input = scanner.nextLine();
        while (!input.equals("e")){

            switch (input) {
                case "createGroup" -> {
                    System.out.print("Group name: ");
                    String groupName = scanner.nextLine();
                    System.out.print("Ordering type: ");
                    String ordering = scanner.nextLine();
                    clientCom.createGroup(groupName, ordering);
                }
                case "joinGroup" -> {
                    System.out.print("Group name: ");
                    clientCom.joinGroup(scanner.nextLine());
                }
                case "listGroups" -> System.out.println(clientCom.listGroups());
                case "sendMessage" -> {
                    System.out.print("Group name: ");
                    String groupName = scanner.nextLine();
                    System.out.print("Message: ");
                    String message = scanner.nextLine();
                    clientCom.sendMessageToGroup(message, groupName);
                }
                case "getGroupMembers" -> {
                    System.out.print("Group name: ");
                    String groupName = scanner.nextLine();
                    System.out.println(clientCom.getGroupMembers(groupName));
                }
                case "intercept" -> {
                    clientCom.debugInterceptMessages(true);
                    System.out.println("Now intercepting");
                }
                case "stopIntercept" -> {
                    clientCom.debugInterceptMessages(false);
                    System.out.println("Stopped intercepting");
                }
                case "releaseAllIntercepted" -> {
                    clientCom.debugReleaseAllIntercepted();
                    System.out.println("All intercepted messages released");
                }
                case "releaseNewestIntercepted" -> {
                    clientCom.debugReleaseNewestIntercepted();
                    System.out.println("Newest intercepted message released");
                }
                case "releaseOldestIntercepted" -> {
                    clientCom.debugReleaseNewestIntercepted();
                    System.out.println("Oldest intercepted message released");
                }
                case "leaveGroup" -> {
                    System.out.print("Group name: ");
                    String groupName = scanner.nextLine();
                    clientCom.leaveGroup(groupName);
                }
            }

            input = scanner.nextLine();
        }
    }
}
