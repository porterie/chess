package model;

public class UserData {
    String username;
    String passwd;
    String email;
    public UserData(String username, String passwd, String email){
        this.username = username;
        this.passwd = passwd;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }
    public String getPasswd() {
        return passwd;
    }
    public String getEmail() {
        return email;
    }
}
