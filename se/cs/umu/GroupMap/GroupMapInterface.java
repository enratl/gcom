package se.cs.umu.GroupMap;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GroupMapInterface extends Remote {
    ArrayList<String> hello() throws RemoteException;
}
