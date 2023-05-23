package se.cs.umu.App;

public class Client {
    public static void main(String[] args) {

        //Run with argument "debugger" or "db" for debugger and anything else for normal client app
        if(args.length < 2) {
            System.out.println("Please specify interface and port");
        }
        else {
            int port = Integer.parseInt(args[1]);
            try {
                if(!args[0].equals("debugger") && !args[0].equals("db")) {
                    ClientGUI clientGUI = new ClientGUI();

                    ClientController controller = new ClientController(clientGUI, port);

                    clientGUI.buildGUI();
                    clientGUI.displayGUI();
                }
                else {
                    DebuggerGUI debuggerGUI = new DebuggerGUI();

                    DebuggerController controller = new DebuggerController(debuggerGUI, port);

                    debuggerGUI.buildGUI();
                    debuggerGUI.displayGUI();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
