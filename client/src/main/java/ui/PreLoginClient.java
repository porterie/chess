package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import server.LoginResult;
import server.RegisterResult;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginClient {
    private final String serverUrl;
    private final ServerFacade server;
    Gson gson = new Gson();
    public PreLoginClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        //failed http will throw the response exception
        if(params.length==3){
            RegisterResult response = server.register(params[0], params[1], params[2]);
            return String.format("Registered user %s", params[0]);
        }else{
            throw new ResponseException(400, "Expected: <username> <password> <email>");
        }
    }

    public String login(String... params) throws ResponseException {
        if(params.length==2){
            LoginResult response = server.login(params[0], params[1]);
            return String.format("Logged in user %s", params[0]);
        }else{
            throw new ResponseException(400, "Expected: <username> <password>");
        }

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
