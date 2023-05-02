package se.cs.umu.App;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        try {
            Debugger debugger = new Debugger();

            DebuggerController controller = new DebuggerController(debugger);

            debugger.buildGUI();
            debugger.displayGUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
