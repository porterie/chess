package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    public static String generateToken() {
        //generates a viable token. Does not add to database!
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest registerRequest){
        RegisterResult result = null;
        try{
            if(userDAO.getUser(registerRequest.username())==null){//username free
                userDAO.createUser(new UserData(registerRequest.username(),
                        registerRequest.passwd(), registerRequest.email()));
                String newUsrToken = generateToken();
                result = new RegisterResult(registerRequest.username(), newUsrToken);
                authDAO.createAuthTokens(newUsrToken, registerRequest.username());//logs the users token
            }else{//username taken
                result = new RegisterResult(null, null);
            }
        }catch(DataAccessException exception){
            System.out.println("ERROR: DATA ACCESS EXCEPTION");
        }
         //unimplemented
        return result;
    }
    public LoginResult login(LoginRequest loginRequest){
        LoginResult loginResult = null;
        try{
            UserData user = userDAO.getUser(loginRequest.username());
            if(user!=null){
                if(Objects.equals(user.getPasswd(), loginRequest.password())){
                    //successful login
                    String loginToken = generateToken();
                    loginResult = new LoginResult(user.getUsername(), loginToken);
                    authDAO.createAuthTokens(loginToken, user.getUsername());
                }else{
                    //incorrect passwd
                    loginResult = new LoginResult(user.getUsername(), null);
                    System.out.println("Incorrect password for login");
                }
            }
        }catch(DataAccessException exception){
            loginResult = null;
            System.out.println(("data access exception"));
        }

        return loginResult;
    }
    public void logout(LogoutRequest logoutRequest){
        try {
            if (authDAO.getAuthUser(logoutRequest.authToken()) != null) {
                String username = authDAO.getAuthUser(logoutRequest.authToken()).getUsername();
                userDAO.deleteUser(username);
                authDAO.deleteAuthTokens(username);
            }
        }catch(DataAccessException exception){
            System.out.println("Data access exception");
        }
    }
}
