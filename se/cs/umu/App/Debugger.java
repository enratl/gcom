package se.cs.umu.App;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Debugger {
    private JButton thisButtonDoesNothingButton;
    private JPanel panel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Debugger");
        frame.setContentPane(new Debugger().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Debugger() {
        thisButtonDoesNothingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Nothing");
            }
        });
    }
}
