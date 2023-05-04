package se.cs.umu.App;

import se.cs.umu.ClientCommunication.ClientCommunicationInterface;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;

import javax.swing.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class DebuggerController extends UnicastRemoteObject implements ClientCommunicationObserver {

    ArrayList<String> memberOf;
    DebuggerGUI debuggerGUI;

    GroupMemberList groupMemberList;

    ClientCommunicationInterface clientCom;
    public DebuggerController(DebuggerGUI debuggerGUI) throws MalformedURLException, NotBoundException, RemoteException {

        this.debuggerGUI = debuggerGUI;

        memberOf = new ArrayList<>();

        clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");
        Registry clientRegistry = LocateRegistry.createRegistry(1103);
        Naming.rebind("//0.0.0.0/Client", this);

        clientCom.addObserver();

        populateGroupList(clientCom.listGroups());

        debuggerGUI.addSendListener(new SendListener());
        debuggerGUI.addCreateGroupListener(new CreateGroupListener());
        debuggerGUI.addJoinListener(new JoinGroupListener());
        debuggerGUI.addSelectGroupListener(new SelectGroupListener());
        debuggerGUI.addInterceptListener(new InterceptListener());
        debuggerGUI.addReleaseNewestListener(new ReleaseNewestListener());
        debuggerGUI.addReleaseOldestListener(new ReleaseOldestListener());
        debuggerGUI.addReleaseAllListener(new ReleaseAllListener());
    }

    @Override
    public void displayMessage(String message, String groupName, String sender, int clientClock) throws RemoteException {
        debuggerGUI.displayMessage(sender + ": " + message);
    }

    @Override
    public void displayOrderingBuffer(String bufferContents) {
        debuggerGUI.displayBuffer(bufferContents);
    }

    private void populateGroupList(ArrayList<String> groups) {
        for (String group : groups) {
            debuggerGUI.addGroup(group);
        }
    }

    class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> groups = new ArrayList<>();
            groups.add("localhost/Communication");

            String message = debuggerGUI.getMessage();
            String group = debuggerGUI.getJoinedGroup();

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
                    System.out.println("Sent message: " + message);
                }
            }
        }
    }

    class CreateGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = debuggerGUI.getGroupName();

            //Create the group in clientCommunication

            if(!group.isEmpty()) {
                try {
                    clientCom.createGroup(group);
                    debuggerGUI.addGroup(group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    class JoinGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = debuggerGUI.getSelectedGroup();

            //Join group through clientCommunication
            if (group != null) {
                try {
                    clientCom.joinGroup(group);
                    debuggerGUI.joinGroup(group);
                    debuggerGUI.displayMessage("Joined group: " + group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    class SelectGroupListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedGroup = debuggerGUI.getSelectedGroup();

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

    class InterceptListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            try {
                if (debuggerGUI.interceptIsChecked()) {
                    clientCom.debugInterceptMessages(true);
                    debuggerGUI.displayMessage("Intercepting messages");
                }
                else {
                    clientCom.debugInterceptMessages(false);
                    debuggerGUI.displayMessage("Stopped intercepting messages");
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    class ReleaseNewestListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                clientCom.debugReleaseNewestIntercepted();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class ReleaseOldestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                clientCom.debugReleaseOldestIntercepted();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class ReleaseAllListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                debuggerGUI.displayBuffer(clientCom.debugGetUndeliveredMessages());
                clientCom.debugReleaseAllIntercepted();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
