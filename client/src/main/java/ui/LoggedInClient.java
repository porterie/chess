package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.CreateGameResult;
import serverfacade.ListGamesResult;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

public class LoggedInClient {
    private final String serverUrl;
    private final ServerFacade server;
    Gson gson = new Gson();
    private LoginState state;
    String username;
    public LoggedInClient(String serverUrl, ServerFacade server){
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
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
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

    public String logout(String... params) throws ResponseException {
        //failed http will throw the response exception
        server.logout(server.getAuthToken());
        state = LoginState.SIGNEDOUT;
        return "Logged out";
    }

    public String createGame(String... params) throws ResponseException {
        if(params.length==1){
            CreateGameResult response = server.createGame(params[0]);
            return String.format("Created game %s", params[0]);
        }else{
            throw new ResponseException("Expected: create <game name>");
        }
    }

    public String listGames(String... params) throws ResponseException {
        //based on petshop listPets()
        ListGamesResult result = server.listGames();
        var list = new StringBuilder();
        Integer i = 1;
        for (var game : result.games()){
            //list.append(game.toString()).append('\n');
            list.append("Game Number: ");
            list.append(i);
            list.append(", White User: ");
            list.append(game.getWhiteUsername());
            list.append(", Black User: ");
            list.append(game.getBlackUsername());
            list.append("\n");
            i++;
        }
        return list.toString();
    }

    public String playGame(String... params) throws ResponseException {
        if(params.length==2 && ((Objects.equals(params[1], "white")) || (Objects.equals(params[1], "black")))) {
            String color;
            if(Objects.equals(params[1], "white")){
                color = "WHITE";
            }else{
                color = "BLACK";
            }
            int internalGameID;
            try {
                internalGameID = Integer.valueOf(params[0]);
            }catch(NumberFormatException e){
                return "Invalid Game ID";
            }
            ListGamesResult gameList = server.listGames();
            boolean foundGame = false;
            int i = 1;
            int serverGameID = -1;
            for (var game : gameList.games()){
                if (i == internalGameID) {
                    foundGame = true;
                    serverGameID = game.getGameID();
                    break;
                }
                i++;
            }
            if(foundGame){
                server.joinGame(color, serverGameID);
                //todo: websocket here
                //DrawBoard display = new DrawBoard(Objects.equals(params[1], "white"), new ChessGame());
                //display.print();

                return "\n";
            }else{
                return "Invalid game ID";
            }
        }
        return "Expected: play <gameID> <WHITE || BLACK> ";
    }

    public String observeGame(String... params) throws ResponseException {
        int  internalGameID;
        try {
            internalGameID = Integer.valueOf(params[0]);
        }catch(NumberFormatException e){
            return "Invalid Game ID";
        }
        ListGamesResult gameList = server.listGames();
        boolean foundGame = false;
        int i = 1;
        int serverGameID = -1;
        for (var game : gameList.games()){
            if (i == internalGameID) {
                foundGame = true;
                serverGameID = game.getGameID();
                break;
            }
            i++;
        }
        if(foundGame){
            DrawBoard display = new DrawBoard(true, new ChessGame());
            display.print();
            return "\n";
        }else{
            return "Invalid game ID";
        }

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
