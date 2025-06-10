package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.LoginResult;
import serverfacade.RegisterResult;
import serverfacade.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {
    private final ServerFacade server;
    private LoginState state = LoginState.SIGNEDOUT; //
    Gson gson = new Gson();
    public PreLoginClient(ServerFacade server){
        this.server = server;
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
            state=LoginState.SIGNEDIN;
            return String.format("Registered user %s", params[0]);
        }else{
            throw new ResponseException("Expected: <username> <password> <email>");
        }
    }

    public String login(String... params) throws ResponseException {
        if(params.length==2){
            LoginResult response = server.login(params[0], params[1]);
            state=LoginState.SIGNEDIN;
            return String.format("Logged in user %s", params[0]);
        }else{
            throw new ResponseException("Expected: <username> <password>");
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
