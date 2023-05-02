package se.cs.umu.App;

import se.cs.umu.Communication.NodeCommunicationInterface;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Debugger {
    private JButton button1;
    private JPanel panel;
    private JTextArea textArea1;
    private JTextField textField1;
    private JLabel label1;
    private JList list1;
    private JTextField textField2;
    private JButton button2;
    private JButton button3;
    private JList list2;
    private JFrame frame;

    DefaultListModel lm1 = new DefaultListModel();

    DefaultListModel lm2 = new DefaultListModel();

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

    public String getGroupName() {
        return textField2.getText();
    }

    public void addSendListener(ActionListener actionListener) {
        button1.addActionListener(actionListener);
    }

    public void addCreateGroupListener(ActionListener actionListener) {
        button2.addActionListener(actionListener);
    }

    public void addJoinListener(ActionListener actionListener) {
        button3.addActionListener(actionListener);
    }

    public void addSelectGroupListener(MouseListener mouseListener) {
        list1.addMouseListener(mouseListener);
    }

    public String getSelectedGroup() {
        return (String) list1.getSelectedValue();
    }

    public String getJoinedGroup() {
        return (String) list2.getSelectedValue();
    }

    public void addGroup(String name) {
        list1.setModel(lm1);
        lm1.addElement(name);
    }

    public void joinGroup(String name) {
        list2.setModel(lm2);
        lm2.addElement(name);
    }
}
