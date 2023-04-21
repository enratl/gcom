package se.cs.umu.App;

import se.cs.umu.Communication.NodeCommunicationInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Debugger {
    private JButton button1;
    private JPanel panel;
    private JTextArea textArea1;
    private JTextField textField1;
    private JFrame frame;



    private static ArrayList<NodeCommunicationInterface> communicators;

    public Debugger() {
    }

    public void displayMessage(String message) {
        textArea1.append(message + "\n");
    }

    public void buildGUI() {
        frame = new JFrame("Debugger");
        frame.setContentPane(this.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public void displayGUI() {
        frame.setVisible(true);
    }

    public String getMessage() {
        return textField1.getText();
    }

    public void addSendListener(ActionListener actionListener) {
        button1.addActionListener(actionListener);
    }
}
