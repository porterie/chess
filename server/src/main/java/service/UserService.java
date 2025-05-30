package service;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    {
        try {
            userDAO = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private AuthDAO authDAO = new MySqlAuthDAO();
    public Boolean authenticationValid(String authToken) throws DataAccessException {
        return authDAO.isAuthToken(authToken);
    }
    public void clearMemory() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
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
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{
        RegisterResult result = null;
        /*try{*/
        if(registerRequest.username()==null || registerRequest.password()==null || registerRequest.email()==null){
            return result;
        }
            if(userDAO.getUser(registerRequest.username())==null){//username free
                String hashPass = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
                userDAO.createUser(new UserData(registerRequest.username(),
                        registerRequest.email(), hashPass));
                System.out.println("Registered user with password: ");
                System.out.println(registerRequest.password());
                String newUsrToken = generateToken();
                result = new RegisterResult(registerRequest.username(), newUsrToken);
                authDAO.createAuthTokens(newUsrToken, registerRequest.username());//logs the users token
            }else{//username taken
                result = new RegisterResult(null, null);
            }
        //}catch(DataAccessException exception){
         /*   System.out.println("DATA ACCESS EXCEPTION");
        }*/

        return result;
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
        LoginResult loginResult = null;
            UserData user = userDAO.getUser(loginRequest.username());
            String hashPass = BCrypt.hashpw(loginRequest.password(), BCrypt.gensalt());
            if(user!=null){
                if(BCrypt.checkpw(loginRequest.password(), user.getPasswd())){
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


        return loginResult;
    }
    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        Boolean userFound = false;
        Boolean success = false;

            if (authDAO.getAuthUser(logoutRequest.authToken()) != null) {
                userFound = true;
                authDAO.deleteAuthTokens(logoutRequest.authToken());
                success = true;
            }

            System.out.println("Data access exception");

        return new LogoutResult(userFound, success);
    }

}
