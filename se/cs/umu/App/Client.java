package se.cs.umu.App;

public class Client {
    public static void main(String[] args) {
        try {
            DebuggerGUI debuggerGUI = new DebuggerGUI();

            DebuggerController controller = new DebuggerController(debuggerGUI);

            debuggerGUI.buildGUI();
            debuggerGUI.displayGUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
