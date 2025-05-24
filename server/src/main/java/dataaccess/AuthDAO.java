package dataaccess;

import model.AuthData;

public interface AuthDAO {

   AuthData getAuthTokens(String username) throws DataAccessException;
   void deleteAuthTokens(String username) throws DataAccessException;
   void createAuthTokens(String authToken, String username) throws DataAccessException;
   AuthData getAuthUser(String authToken) throws DataAccessException;
   Boolean isAuthToken(String authToken);
}
