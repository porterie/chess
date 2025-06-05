package ui;

import server.ServerFacade;

import java.util.Scanner;
import static ui.EscapeSequences.*;


public class PreLoginREPL {
    //based on petshop repl with adjustments for multiple repls
    ServerFacade server; //pass into client instances
    LoggedInREPL loggedInREPL; //nest repls in each other
    PreLoginClient preLoginClient;

    public PreLoginREPL(String serverURL){
        server = new ServerFacade(serverURL);
        preLoginClient = new PreLoginClient(serverURL, server);
        loggedInREPL = new LoggedInREPL(serverURL, server);
    }

    public void run() {
        System.out.println("Welcome to CHESS");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            resetFancyText();
            System.out.print("\n" + ">>>" + SET_TEXT_COLOR_WHITE);
            String line = scanner.nextLine();
            if(preLoginClient.getLoginState()==LoginState.SIGNEDOUT) {
                try {
                    result = preLoginClient.eval(line);
                    System.out.print(SET_TEXT_COLOR_BLUE + result);
                } catch (Throwable e) {
                    String msg = e.toString();
                    System.out.print(msg);
                }
            }else{
                loggedInREPL.run();
            }
        }
        System.out.println();
    }

    private void resetFancyText(){
        System.out.print(RESET_TEXT_ITALIC + RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_BLINKING + RESET_TEXT_UNDERLINE + RESET_TEXT_BOLD_FAINT);
    }
}
