package se.cs.umu.App;

import se.cs.umu.ClientCommunication.ClientCommunicationInterface;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.cert.Certificate;
import java.util.ArrayList;

public class ClientController extends UnicastRemoteObject implements ClientCommunicationObserver {

    ArrayList<String> memberOf;
    ClientGUI gui;

    GroupMemberList groupMemberList;

    ClientCommunicationInterface clientCom;

    protected ClientController(ClientGUI gui, int port) throws RemoteException, MalformedURLException, NotBoundException {
        this.gui = gui;
        memberOf = new ArrayList<>();

        clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");
        Registry clientRegistry = LocateRegistry.createRegistry(port);
        Naming.rebind("//0.0.0.0/Client", this);

        clientCom.addObserver();
        populateGroupList(clientCom.listGroups());

        setListeners();
    }

    public void setListeners() {
        gui.addSendListener(new SendListener());
        gui.addCreateGroupListener(new CreateGroupListener());
        gui.addJoinListener(new JoinGroupListener());
        gui.addLeaveListener(new LeaveGroupListener());
        gui.addRemoveGroupListener(new RemoveGroupListener());
        gui.addRefreshListener(new RefreshListener());
        gui.addSelectGroupListener(new SelectGroupListener());
    }

    private void populateGroupList(ArrayList<String> groups) {
        for (String group : groups) {
            gui.addGroup(group);
        }
    }

    @Override
    public void displayMessage(String message, String groupName, String sender, int clientClock) throws RemoteException {
        gui.displayMessage(sender + ": " + message + " ");
    }

    @Override
    public void displayOrderingBuffer(String bufferContents) throws RemoteException {
    }

    @Override
    public void displayDebugBuffer(String bufferContents) throws RemoteException {
    }

    @Override
    public void displayVectorClocks(String clock) throws RemoteException {
    }

    class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = gui.getMessage();
            String group = gui.getJoinedGroup();

            if (group == null) {
                JOptionPane.showMessageDialog(null, "Please select a group to send to.");
            }
            else {
                if (!group.isEmpty()) {
                    try {
                        clientCom.sendMessageToGroup(message, group);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    class CreateGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = gui.getGroupName();
            String ordering = "";

            if(!group.isEmpty()) {
                try {
                    if (gui.isCausal()) {
                        ordering = "CAUSAL";
                    }
                    else {
                        ordering = "FIFO";
                    }
                    clientCom.createGroup(group, ordering);
                    gui.addGroup(group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    class JoinGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = gui.getSelectedGroup();

            if (group != null) {
                try {
                    clientCom.joinGroup(group);
                    gui.joinGroup(group);
                    gui.displayMessage("Joined group: " + group);
                    memberOf.add(group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select a group to join");
            }
        }
    }

    class LeaveGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = gui.getJoinedGroup();

            if (group != null) {
                try {
                    clientCom.leaveGroup(group);
                    gui.leaveGroup(group);
                    gui.displayMessage("Left group: " + group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select a group to leave");
            }
        }
    }

    class RemoveGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = gui.getSelectedGroup();
            if (group != null) {
                try {
                    clientCom.deleteGroup(group);
                    gui.removeGroup(group);
                    gui.leaveGroup(group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select group to remove");
            }
        }
    }

    class RefreshListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> groups;
            ArrayList<String> temp = new ArrayList<>();

            try {
                groups = clientCom.listGroups();

                gui.updateAvailableGroups(groups);

                for ( String group : groups ) {
                    if (memberOf.contains(group)) {
                        temp.add(group);
                    }
                }

                gui.updateJoinedGroups(temp);
                memberOf = temp;
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class SelectGroupListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedGroup = gui.getSelectedGroup();

                groupMemberList = new GroupMemberList();

                try {
                    ArrayList<String> group = clientCom.getGroupMembers(selectedGroup);

                    for (String member : group ) {
                        groupMemberList.showMember(member);
                        groupMemberList.displayList();
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }


                //Display group members
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {
        }
        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
