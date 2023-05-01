package se.cs.umu.MessageOrdering;

import java.util.HashMap;

public interface MessageOrdering {
    boolean receiveMessage(String sender, String message, HashMap<String, Integer> messageVectorClock);
    HashMap<String, Integer> getVectorClock();
    String getBufferedMessages();
}
