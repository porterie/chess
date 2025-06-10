package ui;

import serverfacade.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class LoggedInREPL {

    //based on petshop repl with adjustments for multiple repls
    ServerFacade server; //pass into client instances
    LoggedInClient loggedInClient;
    public LoggedInREPL(String serverUrl, ServerFacade server){
        this.server = server;
        loggedInClient = new LoggedInClient(serverUrl, server);
    }
    public LoginState getLoginState(){
        return loggedInClient.getLoginState();
    }
    public void setLoginState(LoginState newState){
        loggedInClient.setLoginState(newState);
    }
    public void run() {
        System.out.println("Welcome to CHESS");
        System.out.println(loggedInClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")){
            resetFancyText();
            System.out.print("\n" + ">>>" + SET_TEXT_COLOR_WHITE);
            String line = scanner.nextLine();
            if(loggedInClient.getLoginState()==LoginState.SIGNEDIN) {
                try {
                    result = loggedInClient.eval(line);
                    System.out.println(SET_TEXT_COLOR_BLUE + result);
                } catch (Throwable e) {
                    String msg = e.toString();
                    System.out.println(msg);
                }
            }else{
                break; //should break the while loop, equivalent to a quit from the user
            }
        }
        System.out.println();
    }

    private void resetFancyText(){
        System.out.print(RESET_TEXT_ITALIC + RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_BLINKING + RESET_TEXT_UNDERLINE + RESET_TEXT_BOLD_FAINT);
    }
}
