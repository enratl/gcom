package se.cs.umu.App;

import javax.swing.*;
import java.util.ArrayList;

public class GroupMemberList {
    private JPanel panel1;
    private JList list1;

    private JFrame frame;

    DefaultListModel lm = new DefaultListModel();

    public GroupMemberList() {
    }

    public void displayList(String name) {
        frame = new JFrame(name);
        frame.setContentPane(this.panel1);
        frame.pack();
        frame.setVisible(true);
    }

    public void showMember(String member) {
        list1.setModel(lm);
        lm.addElement(member);
    }
}
