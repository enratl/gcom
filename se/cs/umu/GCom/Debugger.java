package se.cs.umu.GCom;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Debugger {

    private boolean shouldIntercept;

    private Deque<DebugBufferEntry> intercepted;

    private final ConcurrentHashMap<String, int[]> receiveStats;


    public Debugger() {
        shouldIntercept = false;
        intercepted = new ArrayDeque<>();
        receiveStats = new ConcurrentHashMap<>();
    }

    public void intercept(String groupName, String sender, String message, HashMap<String, Integer> messageVectorClock) {
        intercepted.addLast(new DebugBufferEntry(groupName, sender, message, messageVectorClock));
    }

    public DebugBufferEntry releaseOldestIntercepted() {
        return intercepted.pollFirst();
    }

    public DebugBufferEntry releaseNewestIntercepted() {
        return intercepted.pollLast();
    }

    public ArrayList<DebugBufferEntry> releaseAllIntercepted() {
        ArrayList<DebugBufferEntry> tempIntercepted = new ArrayList<>(intercepted);
        intercepted = new LinkedList<>();
        return tempIntercepted;
    }

    public ArrayList<DebugBufferEntry> getDebugBuffer() {
        return new ArrayList<>(intercepted);
    }

    public void setShouldIntercept(boolean shouldIntercept) {
        this.shouldIntercept = shouldIntercept;
    }

    public boolean shouldIntercept() {
        return shouldIntercept;
    }

    public void addSendStatistics(String receiver, boolean wasReceived) {
        if (!receiveStats.containsKey(receiver)) {
            receiveStats.put(receiver, new int[]{0, 0});
        }

        if (wasReceived) {
            receiveStats.get(receiver)[0] += 1;
        }

        receiveStats.get(receiver)[1] += 1;
    }

    public String getSendStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("receiver, received/sent");
        for (String receiver : receiveStats.keySet()) {
            sb.append(receiver).append(", ");
            sb.append(receiveStats.get(receiver)[0]).append(receiveStats.get(receiver)[1]).append("\n");
        }

        return sb.substring(0, sb.length() - 1);
    }
}
