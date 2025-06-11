package ui;

import serverfacade.ServerFacade;
import websocket.messages.ServerLoadGame;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessGameREPL {
    ServerFacade server; //pass into client instances
    ChessGameClient chessGameClient;

    public ChessGameREPL(String serverUrl, ServerFacade server, Integer gameID, ServerLoadGame.PlayerType playerType, String username){
        this.server = server;
        chessGameClient = new ChessGameClient(serverUrl, server, gameID, playerType, username);
    }

    public void run() {
        //System.out.println("Welcome to CHESS");
        System.out.println(chessGameClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {//todo: adjust exit condition
            System.out.print("\n" + ">>>" + SET_TEXT_COLOR_WHITE);
            String line = scanner.nextLine();
            try {
                result = chessGameClient.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                String msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }
}

