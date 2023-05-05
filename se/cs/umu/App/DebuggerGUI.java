package se.cs.umu.App;

import se.cs.umu.Communication.NodeCommunicationInterface;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class DebuggerGUI {
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
    private JCheckBox checkBox1;
    private JButton releaseNewestButton;
    private JButton releaseOldestButton;
    private JButton releaseAllButton;
    private JTextArea textArea2;
    private JButton button4;
    private JRadioButton causalRadioButton;
    private JRadioButton FIFORadioButton;
    private JTextArea textArea3;
    private JButton button5;
    private JTabbedPane tabbedPane1;
    private ButtonGroup buttonGroup1;
    private JFrame frame;

    DefaultListModel lm1 = new DefaultListModel();

    DefaultListModel lm2 = new DefaultListModel();

    private static ArrayList<NodeCommunicationInterface> communicators;

    public DebuggerGUI() {
    }

    public void buildGUI() {
        frame = new JFrame("Debugger");
        frame.setContentPane(this.panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    public void displayMessage(String message) {
        textArea1.append(message + "\n");
    }

    public void displayOrderingBuffer(String bufferContents) {
        textArea2.setText("");
        textArea2.append(bufferContents);

    }

    public void displayDebugBuffer(String bufferContents) {
        textArea3.setText("");
        textArea3.append(bufferContents);
    }

    public void displayVectorClocks(String clocks) {
        //Display values
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

    public void addLeaveListener(ActionListener actionListener) {
        button4.addActionListener(actionListener);
    }

    public void addSelectGroupListener(MouseListener mouseListener) {
        list1.addMouseListener(mouseListener);
    }

    public void addInterceptListener(ItemListener itemListener) {
        checkBox1.addItemListener(itemListener);
    }

    public void addReleaseNewestListener(ActionListener actionListener) {
        releaseNewestButton.addActionListener(actionListener);
    }

    public void addReleaseOldestListener(ActionListener actionListener) {
        releaseOldestButton.addActionListener(actionListener);
    }

    public void addReleaseAllListener(ActionListener actionListener) {
        releaseAllButton.addActionListener(actionListener);
    }

    public void addRemoveGroupListener(ActionListener actionListener) {
        button5.addActionListener(actionListener);
    }

    public String getSelectedGroup() {
        return (String) list1.getSelectedValue();
    }

    public String getJoinedGroup() {
        return (String) list2.getSelectedValue();
    }

    public boolean interceptIsChecked() {
        return checkBox1.isSelected();
    }

    public boolean getSelectedOrdering() {
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
}
