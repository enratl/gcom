package se.cs.umu.App;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ExplicitJoin {
    private JPanel panel1;
    private JButton explicitJoinButton;
    private JTextArea textArea1;
    private JRadioButton causalRadioButton;
    private JRadioButton FIFORadioButton;

    private JFrame frame;

    public ExplicitJoin() {
    }

    public void displayExplicitJoin() {
        frame = new JFrame("Explicit Join");
        frame.setContentPane(this.panel1);
        frame.pack();
        frame.setVisible(true);
    }

    public void closeWindow() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public boolean causalSelected() {
        return causalRadioButton.isSelected();
    }

    public String getGroupInfo() {
        return textArea1.getText();
    }

    public void addJoinListener(ActionListener actionListener) {
        explicitJoinButton.addActionListener(actionListener);
    }
}
