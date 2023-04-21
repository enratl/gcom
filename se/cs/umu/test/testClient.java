package se.cs.umu.test;

import se.cs.umu.App.Debugger;
import se.cs.umu.App.DebuggerController;
import se.cs.umu.Communication.NodeCommunicationInterface;

import javax.swing.*;
import java.rmi.Naming;
import java.util.ArrayList;

public class testClient {
    public static void main(String[] args) {
        Debugger debugger = new Debugger();
        debugger.buildGUI();
        DebuggerController debuggerController = new DebuggerController(debugger);

        debugger.displayGUI();

        try {

            //GroupMapInterface groupMap = (GroupMapInterface) Naming.lookup("rmi://localhost/GroupMap");

            //ArrayList<String> list = groupMap.hello();
            //System.out.println(list);

            //communicator.send(communicators, "Ding dong");
            //String response = communicator.receive();
            //System.out.println("Received message: " + response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
