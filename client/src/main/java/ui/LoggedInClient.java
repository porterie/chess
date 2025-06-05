package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import org.glassfish.grizzly.http.server.Response;
import server.*;

import java.util.Arrays;

public class LoggedInClient {
    private final String serverUrl;
    private final ServerFacade server;
    Gson gson = new Gson();
    private LoginState state;
    public LoggedInClient(String serverUrl, ServerFacade server){
        this.serverUrl = serverUrl;
        this.server = server;
        state = LoginState.SIGNEDIN;
    }

    public LoginState getLoginState(){
        return state;
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
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
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
            throw new ResponseException(400, "Expected: create <game name>");
        }
    }

    public String listGames(String... params) throws ResponseException {
        //based on petshop listPets()
        ListGamesResult result = server.listGames();
        var list = new StringBuilder();
        for (var game : result.games()){
            list.append(gson.toJson(game)).append('\n');
        }
        return list.toString();
    }

    public String playGame(String... params) throws ResponseException {
        //TODO: remains to be implemented. For phase 5 do basic display
        return "Not implemented";
    }

    public String observeGame(String... params) throws ResponseException {
        //TODO: remains to be implemented. For phase 5 do basic display
        return "Not implemented";
    }

    public String help() {
        return """
                - register <username> <password> <email>
                - login <username> <password>
                - quit
                - help
                """;
    }
}
