package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.CreateGameResult;
import serverfacade.ListGamesResult;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

public class ChessGameClient  {
    private final String serverUrl;
    private final ServerFacade server;
    Gson gson = new Gson();
    private LoginState state;
    public ChessGameClient(String serverUrl, ServerFacade server){
        this.serverUrl = serverUrl;
        this.server = server;
        state = LoginState.SIGNEDIN;
    }

    public LoginState getLoginState(){
        return state;
    }
    public void setLoginState(LoginState newState){
        state = newState;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave(params);
                case "move" -> makeMove(params);
                case "resign" -> resign(params);
                case "highlight" -> hightLightMoves(params);
                case "quit" ->  quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String quit() {
        System.exit(0);
        return "quit";
    }



    public String help() {
        return """
                - create <game name>
                - list
                - play <gameID> <WHITE|BLACK>
                - observe <gameID>
                - logout
                - quit
                - help
                """;
    }
}
