package ui;

import exception.ResponseException;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginClient {
    private final String serverUrl;
    private final ServerFacade server;
    private LoginState state = LoginState.SIGNEDOUT;
    public PreLoginClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
        //TODO: add notification handler if neccessary
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {//todo: correct methods for class
                case "signin" -> register(params);
                case "rescue" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if(params.length==3){
            boolean registerResult = server.register(params[0], params[1], params[2]);
            if(registerResult){
                return "positive register";
            }else{
                return "negative register";
            }
        }else{
            throw new ResponseException(500, "Error: invalid inputs for registration");
        }
    }

}
