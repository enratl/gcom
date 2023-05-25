package se.cs.umu.App;

import se.cs.umu.ClientCommunication.ClientCommunicationInterface;
import se.cs.umu.ClientCommunication.ClientCommunicationObserver;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebuggerController extends UnicastRemoteObject implements ClientCommunicationObserver {

    ArrayList<String> memberOf;
    DebuggerGUI debuggerGUI;

    GroupMemberList groupMemberList;

    ClientCommunicationInterface clientCom;

    ExplicitJoin explicitJoin;
    public DebuggerController(DebuggerGUI debuggerGUI, int port) throws MalformedURLException, NotBoundException, RemoteException {

        this.debuggerGUI = debuggerGUI;

        explicitJoin = new ExplicitJoin();

        memberOf = new ArrayList<>();

        clientCom = (ClientCommunicationInterface) Naming.lookup("rmi://localhost/ClientCom");
        Registry clientRegistry = LocateRegistry.createRegistry(port);
        Naming.rebind("//0.0.0.0/Client", this);

        clientCom.addObserver();

        populateGroupList(clientCom.listGroups());

        setListeners();
    }

    private void setListeners() {
        debuggerGUI.addSendListener(new SendListener());
        debuggerGUI.addCreateGroupListener(new CreateGroupListener());
        debuggerGUI.addJoinListener(new JoinGroupListener());
        debuggerGUI.addSelectGroupListener(new SelectGroupListener());
        debuggerGUI.addInterceptListener(new InterceptListener());
        debuggerGUI.addReleaseNewestListener(new ReleaseNewestListener());
        debuggerGUI.addReleaseOldestListener(new ReleaseOldestListener());
        debuggerGUI.addReleaseAllListener(new ReleaseAllListener());
        debuggerGUI.addLeaveListener(new LeaveGroupListener());
        debuggerGUI.addRemoveGroupListener(new RemoveGroupListener());
        debuggerGUI.addRefreshListener(new RefreshListener());
        debuggerGUI.addPopupListener(new PopupListener());
        //debuggerGUI.addDropListener(new DropListener());
        explicitJoin.addJoinListener(new ExplicitJoinListener());
    }

    @Override
    public void displayMessage(String message, String groupName, String sender, int clientClock) throws RemoteException {
        debuggerGUI.displayMessage(sender + ": " + message + " " + clientClock);
        debuggerGUI.displayVectorClocks(clientCom.debugGetVectorClocks());
    }

    @Override
    public void displayOrderingBuffer(String bufferContents) {
        debuggerGUI.displayOrderingBuffer(bufferContents);
    }

    @Override
    public void displayDebugBuffer(ArrayList<String> bufferContents) {
        debuggerGUI.displayDebugBuffer(bufferContents);
    }

    @Override
    public void displayVectorClocks(String clocks) {
        debuggerGUI.displayVectorClocks(clocks);
    }

    @Override
    public void displaySendStatistics(String statistics) {
        debuggerGUI.displayMessage(statistics);
    }

    @Override
    public void displayExplicitJoin() {
        explicitJoin.displayExplicitJoin();
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
                        debuggerGUI.displayVectorClocks(clientCom.debugGetVectorClocks());
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
            String ordering = "";

            //Create the group in clientCommunication

            if(!group.isEmpty()) {
                try {
                    if (debuggerGUI.getSelectedOrdering()) {
                        ordering = "CAUSAL";
                    }
                    else {
                        ordering = "FIFO";
                    }
                    clientCom.createGroup(group, ordering);
                    debuggerGUI.addGroup(group);
                    //debuggerGUI.displayMessage("Created group " + group + " with ordering " + ordering);
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
                    if(clientCom.joinGroup(group)) {
                        debuggerGUI.joinGroup(group);
                        debuggerGUI.displayMessage("Joined group: " + group);
                        memberOf.add(group);
                    }
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
            String group = debuggerGUI.getJoinedGroup();

            if (group != null) {
                try {
                    clientCom.leaveGroup(group);
                    debuggerGUI.leaveGroup(group);
                    debuggerGUI.displayMessage("Left group: " + group);
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
            String group = debuggerGUI.getSelectedGroup();
            if (group != null) {
                try {
                    clientCom.deleteGroup(group);
                    debuggerGUI.removeGroup(group);
                    debuggerGUI.leaveGroup(group);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please select group to remove");
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
                    }
                    groupMemberList.displayList(selectedGroup);
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
                debuggerGUI.displayOrderingBuffer(clientCom.debugGetUndeliveredMessages());
                clientCom.debugReleaseAllIntercepted();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
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

                debuggerGUI.updateAvailableGroups(groups);
                debuggerGUI.displayOrderingBuffer(clientCom.debugGetUndeliveredMessages());
                debuggerGUI.displayVectorClocks(clientCom.debugGetVectorClocks());

                for ( String group : groups ) {
                    if (memberOf.contains(group)) {
                        temp.add(group);
                    }
                }

                debuggerGUI.updateJoinedGroups(temp);
                memberOf = temp;
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    class PopupListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                debuggerGUI.showPopup(new DropListener());
            }
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

    class DropListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int messageIndex = debuggerGUI.getInterceptedMessage();

            try {
                clientCom.debugDropMessage(messageIndex);
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(null, "Could not drop message");
            }
        }
    }

    class ExplicitJoinListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String ordering;
            if (explicitJoin.causalSelected()) {
                ordering = "CAUSAL";
            }
            else {
                ordering = "FIFO";
            }

            String groupInfo = explicitJoin.getGroupInfo();

            String[] temp = groupInfo.split("\n");
            ArrayList<String> groupMembers = new ArrayList<>(Arrays.asList(temp).subList(1, temp.length));

            try {
                if(clientCom.explicitJoin(temp[0], groupMembers, ordering)) {
                    debuggerGUI.joinGroup(temp[0]);
                    debuggerGUI.displayMessage("Joined group: " + temp[0]);
                    memberOf.add(temp[0]);
                }
            } catch (RemoteException ex) {
                System.out.println("too bad");
            }

            explicitJoin.closeWindow();
        }
    }
}
