package se.cs.umu.test;

import se.cs.umu.App.Debugger;
import se.cs.umu.App.DebuggerController;
import se.cs.umu.ClientCommunication.ClientCommunication;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;
import se.cs.umu.Communication.NodeCommunicationInterface;

import javax.swing.*;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class testClient extends UnicastRemoteObject implements ClientCommunicationObserver {
    public testClient() throws RemoteException {
        super();
    }

    @Override
    public void update(String message, String groupName, String sender, int clientClock) throws RemoteException{
        System.out.println("Client received message: " + message + "\n");
    }
}
