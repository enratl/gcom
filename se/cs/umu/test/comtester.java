package se.cs.umu.test;



import se.cs.umu.ClientCommunication.ClientCommunicationInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class comtester {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        ClientCommunicationInterface clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");

        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        while (!input.equals("e")){

            switch (input) {
                case "createGroup" -> {
                    System.out.print("Group name: ");
                    clientCom.createGroup(scanner.nextLine());
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
            }

            input = scanner.nextLine();
        }


    }
}
