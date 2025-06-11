package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.CreateGameResult;
import serverfacade.ListGamesResult;
import serverfacade.ServerFacade;
import websocket.messages.ServerLoadGame;

import java.util.Arrays;
import java.util.Objects;

public class ChessGameClient  {
    private final String serverUrl;
    private final ServerFacade server;
    Gson gson = new Gson();
    Integer gameID;
    ServerLoadGame.PlayerType playerType;
    public ChessGameClient(String serverUrl, ServerFacade server, Integer gameID, ServerLoadGame.PlayerType playerType){
        this.serverUrl = serverUrl;
        this.server = server;
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
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redraw(){
        ChessGame game = server.
        if(playerType== ServerLoadGame.PlayerType.BLACK){

        }else{

        }
    }


    public String help() {
        return """
                - chessgame help menu (fix me)
                """;
    }
}
