package se.cs.umu.GCom;

import java.util.HashMap;

public record DebugBufferEntry(String groupName, String sender, String message,
                               HashMap<String, Integer> messageVectorClock) {
}
