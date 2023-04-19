package se.cs.umu.Communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Communicator extends UnicastRemoteObject implements CommunicationInterface{

    protected Communicator() throws RemoteException {

    }

}
