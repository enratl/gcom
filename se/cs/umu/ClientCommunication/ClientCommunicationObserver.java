package se.cs.umu.ClientCommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientCommunicationObserver extends Remote{
    void displayMessage(String message, String groupName, String sender, int clientClock) throws RemoteException;

    void displayOrderingBuffer(String bufferContents) throws RemoteException;

    void displayDebugBuffer(ArrayList<String> bufferContents) throws RemoteException;

    void displayVectorClocks(String clock) throws RemoteException;

    void displaySendStatistics(String statistics) throws RemoteException;

    void displayExplicitJoin() throws RemoteException;
}
