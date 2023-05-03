package se.cs.umu.MessageOrdering;

import se.cs.umu.GCom.GCom;

import java.util.HashMap;

public class UnorderedOrdering implements MessageOrdering {

    private int clientClock;
    private final String groupName;
    private final GCom gcom;

    public UnorderedOrdering(String groupName, GCom gcom) {
        clientClock = 0;
        this.groupName = groupName;
        this.gcom = gcom;
    }

    @Override
    public boolean receiveMessage(String sender, String message, HashMap<String, Integer> messageVectorClock) {
        clientClock++;
        gcom.deliverMessage(message, groupName, sender, clientClock);
        return true;
    }

    @Override
    public HashMap<String, Integer> getVectorClock() {
        return new HashMap<>();
    }

    @Override
    public String getBufferedMessages() {
        return "";
    }
}
