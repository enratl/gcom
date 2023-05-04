package se.cs.umu.App;

import se.cs.umu.ClientCommunication.ClientCommunicationInterface;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;
import se.cs.umu.Communication.NodeCommunication;

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
    Debugger debugger;

    GroupMemberList groupMemberList;

    ClientCommunicationInterface clientCom;
    public DebuggerController(Debugger debugger) throws MalformedURLException, NotBoundException, RemoteException {

        this.debugger = debugger;

        memberOf = new ArrayList<>();

        clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");
        Registry clientRegistry = LocateRegistry.createRegistry(1103);
        Naming.rebind("//0.0.0.0/Client", this);

        clientCom.addObserver();

        populateGroupList(clientCom.listGroups());

        debugger.addSendListener(new SendListener());
        debugger.addCreateGroupListener(new CreateGroupListener());
        debugger.addJoinListener(new JoinGroupListener());
        debugger.addSelectGroupListener(new SelectGroupListener());
        debugger.addInterceptListener(new InterceptListener());
        debugger.addReleaseNewestListener(new ReleaseNewestListener());
        debugger.addReleaseOldestListener(new ReleaseOldestListener());
        debugger.addReleaseAllListener(new ReleaseAllListener());
    }

    @Override
    public void update(String message, String groupName, String sender, int clientClock) throws RemoteException {
        System.out.println("here");
        debugger.displayMessage(sender + ": " + message);
    }

    private void populateGroupList(ArrayList<String> groups) {
        for (String group : groups) {
            debugger.addGroup(group);
        }
    }

    class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<String> groups = new ArrayList<>();
            groups.add("localhost/Communication");

            String message = debugger.getMessage();
            String group = debugger.getJoinedGroup();

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
            String group = debugger.getGroupName();

            //Create the group in clientCommunication

            if(!group.isEmpty()) {
                try {
                    clientCom.createGroup(group);
                    debugger.addGroup(group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    class JoinGroupListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String group = debugger.getSelectedGroup();

            //Join group through clientCommunication
            if (group != null) {
                try {
                    clientCom.joinGroup(group);
                    debugger.joinGroup(group);
                    debugger.displayMessage("Joined group: " + group);
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
                String selectedGroup = debugger.getSelectedGroup();

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
                if (debugger.interceptIsChecked()) {
                    clientCom.debugInterceptMessages(true);
                    debugger.displayMessage("Intercepting messages");
                }
                else {
                    clientCom.debugInterceptMessages(false);
                    debugger.displayMessage("Stopped intercepting messages");
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
                clientCom.debugReleaseAllIntercepted();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
