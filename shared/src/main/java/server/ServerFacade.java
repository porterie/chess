package server;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.Collection;

import com.google.gson.JsonObject;
import exception.ResponseException;
import model.GameData;
import model.AuthData;
import model.UserData;



public class ServerFacade {
    //based on petshop implementation

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    //implement all possible http requests.
    public String register(String username, String password, String email) throws ResponseException {
        //should return authToken?
        var path = "/user";
        UserData user = new UserData(username, email, password);
        JsonObject registerResult = this.makeRequest("POST", path, user, JsonObject.class, null);
        return registerResult.get("authToken").getAsString(); //returns authtoken
    }

    public String login(String username, String password) throws ResponseException {
        var path = "/session";
        UserData user = new UserData(username, null, password);
        JsonObject loginResult = this.makeRequest("POST", path, user, JsonObject.class, null);
        return loginResult.get("authToken").getAsString();
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public GameData createGame(String gameName){
        var path = "/game";
        GameData newGame = new GameData(null, null, null, gameName, null);
        JsonObject createGameResult = this.makeRequest("POST", path, newGame, )
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
