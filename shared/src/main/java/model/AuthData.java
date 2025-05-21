package model;

public class AuthData {
    String authToken;
    String username;
    public AuthData(String authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }
    public String getAuthToken(){
        return authToken;
    }
    public String getUsername(){
        return username;
    }
}
