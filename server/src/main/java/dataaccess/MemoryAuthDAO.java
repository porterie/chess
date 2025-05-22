package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    final private ArrayList<AuthData> tokens = new ArrayList<>();

    public AuthData getAuthTokens(String username) throws DataAccessException {
        AuthData authToken = null;
        for (AuthData token : tokens){
            if(Objects.equals(token.getUsername(), username)){
                authToken = token;
            }
        }
        return authToken;
    }
    public AuthData getAuthUser(String authToken){
        AuthData returnToken = null;
        for (AuthData token : tokens){
            if(Objects.equals(token.getAuthToken(), authToken)){
                returnToken = token;
            }
        }
        return returnToken;
    }
    public void deleteAuthTokens(String username) throws DataAccessException {

    }

    public void createAuthTokens(String authToken, String username) throws DataAccessException {
        tokens.add(new AuthData(authToken, username));
    }
}
