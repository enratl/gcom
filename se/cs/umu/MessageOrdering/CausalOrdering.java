package se.cs.umu.MessageOrdering;

import se.cs.umu.GCom.GCom;

import java.util.ArrayList;
import java.util.HashMap;

public class CausalOrdering implements MessageOrdering{
    private final ArrayList<BufferEntry> buffer;
    private final HashMap<String, Integer> vectorClock;
    private final GCom gcom;
    private final String groupName;

    private int clientClock;

    public CausalOrdering(HashMap<String, Integer> vectorClock, String groupName, GCom gcom) {
        buffer = new ArrayList<>();
        this.vectorClock = vectorClock;
        this.groupName = groupName;
        this.gcom = gcom;
        clientClock = 0;
        System.out.println(vectorClock);
    }

    public boolean receiveMessage(String sender, String message, HashMap<String, Integer> messageVectorClock) {
        // If it's the first time we see this member, assume the next message to receive is nr 1.
        if (!vectorClock.containsKey(sender)) {
            vectorClock.put(sender, 0);
        }

        if (messageIsReadyToDeliver(sender, messageVectorClock)) {
            deliverMessage(message, sender);
            vectorClock.replaceAll((m, v) -> Math.max(vectorClock.get(m), messageVectorClock.getOrDefault(m,0)));

            // Check if delivering this message made it possible to deliver any message in the buffer
            buffer.removeIf(bufferEntry -> receiveMessage(bufferEntry.sender, bufferEntry.message, bufferEntry.messageVectorClock));

            return true;
        } else {
            System.out.println("message wasn't ready to deliver, sending to buffer");
            buffer.add(new BufferEntry(sender, message, messageVectorClock));
            return false;
        }
    }

    public HashMap<String, Integer> getVectorClock() {
        return vectorClock;
    }

    private boolean messageIsReadyToDeliver(String sender, HashMap<String, Integer> messageVectorClock) {
        if (sender.equals(gcom.getUsername()) && messageVectorClock.get(sender).equals(vectorClock.get(sender))){
            // this is our own message
            return true;
        }

        if (messageVectorClock.get(sender) != vectorClock.get(sender) + 1) {
            return false;
        }

        for (String member : vectorClock.keySet()) {

            if (!messageVectorClock.containsKey(member)){
                continue;
            }

            if (!member.equals(sender) && (messageVectorClock.get(member) > vectorClock.get(member))) {
                return false;
            }
        }

        return true;
    }

    private void deliverMessage(String message, String sender) {
        clientClock++;
        gcom.deliverMessage(message, groupName, sender, clientClock);
    }

    private record BufferEntry(String sender, String message, HashMap<String, Integer> messageVectorClock) {
    }
}
