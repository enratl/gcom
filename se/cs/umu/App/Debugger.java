package se.cs.umu.App;

import se.cs.umu.Communication.CommunicationInterface;
import se.cs.umu.Communication.Communicator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Debugger {
    private JButton button1;
    private JPanel panel;
    private JTextArea textArea1;
    private JTextField textField1;

    private static ArrayList<CommunicationInterface> communicators = new ArrayList<>();
    private static Communicator communicator;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Debugger");
        frame.setContentPane(new Debugger().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            CommunicationInterface communication = (CommunicationInterface) Naming.lookup("rmi://localhost/Communication");
            communicator = new Communicator(communication);

            communicators.add(communication);

            communicator.send(communicators, "Ding dong");
            String response = communicator.receive();

            System.out.println("Received message: " + response);

        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Debugger() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textField1.getText();
                String response = "";
                try {
                    communicator.send(communicators, message);
                    response = communicator.receive();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(null, "Could not send message");
                }
                textArea1.append(response + "\n");
            }
        });
    }
}
