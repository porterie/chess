package dataaccess;

import model.AuthData;

public interface AuthDAO {

   AuthData getAuthTokens() throws DataAccessException;
   void deleteAuthTokens() throws DataAccessException;
   AuthData createAuthTokens() throws DataAccessException;
}
