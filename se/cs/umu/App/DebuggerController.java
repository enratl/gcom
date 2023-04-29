package se.cs.umu.App;

import se.cs.umu.Communication.NodeCommunication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class DebuggerController {

    Debugger debugger;
    NodeCommunication nodeCommunication;
    public DebuggerController(Debugger debugger) {
        try {
            nodeCommunication = new NodeCommunication(null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        this.debugger = debugger;

        debugger.addSendListener(new SendListener());
    }

    public void displayMessage(String message) {
        debugger.displayMessage(message);
    }

    class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> group = new ArrayList<>();
            group.add("localhost/Communication");

            String message = debugger.getMessage();

            //nodeCommunication.sendToNodes(message, group);
            System.out.println("Sent message: " + message);
        }
    }
}
