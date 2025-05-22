package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
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
        return null; //unimplemented
    }
    public void logout(LogoutRequest logoutRequest){
        //unimplemented
    }
}
