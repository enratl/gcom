package se.cs.umu.App;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ClientGUI {
    private JTextArea textArea1;
    private JTextField textField1;
    private JList list1;
    private JList list2;
    private JTextField textField2;
    private JRadioButton causalRadioButton;
    private JRadioButton FIFORadioButton;
    private JCheckBox checkBox1;
    private JButton button4;
    private JButton button3;
    private JButton button5;
    private JButton removeGroupButton;
    private JButton sendButton;
    private JButton releaseAllButton;
    private JButton releaseNewestButton;
    private JButton releaseOldestButton;

    private JFrame frame;
    private JPanel panel;
    private JButton createGroupButton;
    private JButton joinGroupButton;
    private JButton leaveGroupButton;
    private JButton refreshButton;

    private ButtonGroup buttonGroup1;

    DefaultListModel lm1 = new DefaultListModel();

    DefaultListModel lm2 = new DefaultListModel();

    public ClientGUI() {

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

    public void displayMessage(String message) {
        textArea1.append(message + "\n");
    }

    public void updateAvailableGroups(ArrayList<String> groups) {
        list1.setModel(lm1);
        lm1.clear();
        lm1.addAll(groups);
    }

    public void updateJoinedGroups(ArrayList<String> groups) {
        list2.setModel(lm2);
        lm2.clear();
        lm2.addAll(groups);
    }

    public String getMessage() {
        return textField1.getText();
    }

    public String getGroupName() {
        return textField2.getText();
    }

    public String getSelectedGroup() {
        return (String) list1.getSelectedValue();
    }

    public String getJoinedGroup() {
        return (String) list2.getSelectedValue();
    }

    public boolean isCausal() {
        return causalRadioButton.isSelected();
    }

    public void addGroup(String name) {
        list1.setModel(lm1);
        lm1.addElement(name);
    }

    public void removeGroup(String name) {
        list1.setModel(lm1);
        lm1.removeElement(name);
    }

    public void joinGroup(String name) {
        list2.setModel(lm2);
        lm2.addElement(name);
    }

    public void leaveGroup(String name) {
        list2.setModel(lm2);
        lm2.removeElement(name);
    }

    public void addSendListener(ActionListener actionListener) {
        sendButton.addActionListener(actionListener);
    }

    public void addCreateGroupListener(ActionListener actionListener) {
        createGroupButton.addActionListener(actionListener);
    }

    public void addJoinListener(ActionListener actionListener) {
        joinGroupButton.addActionListener(actionListener);
    }

    public void addLeaveListener(ActionListener actionListener) {
        leaveGroupButton.addActionListener(actionListener);
    }

    public void addRemoveGroupListener(ActionListener actionListener) {
        removeGroupButton.addActionListener(actionListener);
    }

    public void addRefreshListener(ActionListener actionListener) {
        refreshButton.addActionListener(actionListener);
    }

    public void addSelectGroupListener(MouseListener mouseListener) {
        list1.addMouseListener(mouseListener);
    }

}
