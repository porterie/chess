package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO = new MemoryUserDAO();
    private AuthDAO authDAO = new MemoryAuthDAO();
    public Boolean authenticationValid(String authToken) throws DataAccessException {
        return authDAO.isAuthToken(authToken);
    }
    public void clearMemory() throws DataAccessException {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
    }
    public String getUser(String auth) throws DataAccessException {
        //gets username from auth token
        AuthData user = authDAO.getAuthUser(auth);
        return user.getUsername();
    }
    public static String generateToken() {
        //generates a viable token. Does not add to database!
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest registerRequest){
        RegisterResult result = null;
        try{
            if(userDAO.getUser(registerRequest.username())==null){//username free
                userDAO.createUser(new UserData(registerRequest.username(),
                        registerRequest.email(), registerRequest.password()));
                System.out.println("Registered user with password: ");
                System.out.println(registerRequest.password());
                String newUsrToken = generateToken();
                result = new RegisterResult(registerRequest.username(), newUsrToken);
                authDAO.createAuthTokens(newUsrToken, registerRequest.username());//logs the users token
            }else{//username taken
                result = new RegisterResult(null, null);
            }
        }catch(DataAccessException exception){
            System.out.println("ERROR: DATA ACCESS EXCEPTION");
        }
         if(registerRequest.username()==null || registerRequest.password()==null || registerRequest.email()==null){
             result = null;
         }
        return result;
    }
    public LoginResult login(LoginRequest loginRequest) {
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
            }else{
                loginResult = new LoginResult(null, null);
            }
        }catch(DataAccessException exception){
            loginResult = null;
            System.out.println(("data access exception"));
        }
        return loginResult;
    }
    public LogoutResult logout(LogoutRequest logoutRequest){
        Boolean userFound = false;
        Boolean success = false;
        try {
            if (authDAO.getAuthUser(logoutRequest.authToken()) != null) {
                userFound = true;
                //String username = authDAO.getAuthUser(logoutRequest.authToken()).getUsername();
                //userDAO.deleteUser(username);
                authDAO.deleteAuthTokens(logoutRequest.authToken());
                success = true;
            }
        }catch(DataAccessException exception){
            System.out.println("Data access exception");
        }
        return new LogoutResult(userFound, success);
    }
}
