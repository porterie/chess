package model;

public class UserData {
    String username;
    String password;
    String email;
    public UserData(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }
    public String getPasswd() {
        return password;
    }
    public String getEmail() {
        return email;
    }
}
