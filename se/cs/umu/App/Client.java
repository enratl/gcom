package se.cs.umu.App;

public class Client {
    public static void main(String[] args) {

        //Run with argument "debugger" for debugger and anything else for normal client app

        try {
            if(args[0].equals("debugger")) {
                DebuggerGUI debuggerGUI = new DebuggerGUI();

                DebuggerController controller = new DebuggerController(debuggerGUI);

                debuggerGUI.buildGUI();
                debuggerGUI.displayGUI();
            }
            else {
                ClientGUI clientGUI = new ClientGUI();

                ClientController controller = new ClientController(clientGUI);

                clientGUI.buildGUI();
                clientGUI.displayGUI();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
