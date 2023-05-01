package se.cs.umu.GCom;

import java.util.*;

public class Debugger {

    private boolean shouldIntercept;

    private Deque<DebugBufferEntry> intercepted;


    public Debugger() {
        shouldIntercept = false;
        intercepted = new ArrayDeque<>();
    }

    public void intercept(String groupName, String sender, String message, HashMap<String, Integer> messageVectorClock) {
        intercepted.addLast(new DebugBufferEntry(groupName, sender, message, messageVectorClock));
    }

    public DebugBufferEntry releaseOldestIntercepted(){
        return intercepted.pollFirst();
    }

    public DebugBufferEntry releaseNewestIntercepted(){
        return intercepted.pollLast();
    }

    public ArrayList<DebugBufferEntry> releaseAllIntercepted() {
        ArrayList<DebugBufferEntry> tempIntercepted = new ArrayList<>(intercepted);
        intercepted = new LinkedList<>();
        return tempIntercepted;
    }

    public void setShouldIntercept(boolean shouldIntercept) {
        this.shouldIntercept = shouldIntercept;
    }

    public boolean shouldIntercept() {
        return shouldIntercept;
    }
}
