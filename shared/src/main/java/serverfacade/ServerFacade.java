package serverfacade;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;

import exception.ResponseException;



public class ServerFacade {
    //based on petshop implementation

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public String getAuthToken(){
        return authToken;
    }
    //implement all possible http requests.
    public RegisterResult register(String username, String password, String email) throws ResponseException {
        //should return authToken?
        var path = "/user";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = this.makeRequest("POST", path, registerRequest, RegisterResult.class, null);
        authToken = registerResult.authToken();
        return registerResult; //returns authtoken
    }

    public LoginResult login(String username, String password) throws ResponseException {
        var path = "/session";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = this.makeRequest("POST", path, loginRequest, LoginResult.class, null);
        authToken = loginResult.authToken();
        return loginResult;
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public CreateGameResult createGame(String gameName) throws ResponseException {
        var path = "/game";
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);
        return this.makeRequest("POST", path, createGameRequest, CreateGameResult.class, authToken);
    }

    public void joinGame(String playerColor, Integer gameID) throws ResponseException {
        var path = "/game";
        JoinGameRequest joinGameRequest = new JoinGameRequest(gameID, playerColor);
        this.makeRequest("PUT", path, joinGameRequest, null, authToken);
    }

    public ListGamesResult listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class, authToken);
    }



// following methods based on petshop equivalent ServerFacade class
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        //default exeption throw, don't want to add uneccessary maven dep
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            //adding support for authToken
            if(authToken!=null){
                http.setRequestProperty("Authorization",authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
