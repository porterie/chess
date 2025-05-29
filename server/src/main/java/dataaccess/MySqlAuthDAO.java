package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthTokens(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT json FROM authentication WHERE username=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(var rs = ps.executeQuery()){
                    if(rs.next()){
                        var userJson = rs.getString("json");
                        return new Gson().fromJson(userJson, AuthData.class);
                    }
                }
            }
        }catch(SQLException exception){
            throw new DataAccessException("Database access exception getAuthTokens");
        }
        System.out.println("ALERT: getAuthTokens returning null.");
        return null;
    }

    @Override
    public void deleteAuthTokens(String username) throws DataAccessException {
        var statement = "DELETE FROM authentication WHERE username=?";
        executeUpdate(statement, username);
    }

    @Override
    public void createAuthTokens(String authToken, String username) throws DataAccessException {
        var statement = "INSERT INTO authentication (authToken, password, json) VALUES (?, ?, ?)";
        AuthData authData = new AuthData(authToken, username);
        var json = new Gson().toJson(authData);
        executeUpdate(statement, authToken, username, json);
        //todo PASSWORD hashing
    }

    @Override
    public AuthData getAuthUser(String authToken) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT json FROM authentication WHERE authToken=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try(var rs = ps.executeQuery()){
                    if(rs.next()){
                        var userJson = rs.getString("json");
                        return new Gson().fromJson(userJson, AuthData.class);
                    }
                }
            }
        }catch(SQLException exception){
            throw new DataAccessException("Database access exception getAuthUser");
        }
        System.out.println("ALERT: getAuthUser returning null.");
        return null;
    }

    @Override
    public Boolean isAuthToken(String authToken) {
        try {
            return getAuthUser(authToken) != null;
        }catch(DataAccessException exception){
            System.out.println("ERROR: data access exception in isAuthToken");
            return false;
        }
    }
    public void clear(){
        var statement = "TRUNCATE authentication";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if(param == null){ps.setNull(i+1, NULL);}
                }
                ps.executeUpdate();

            }
        }catch (SQLException exception) {
            throw new DataAccessException("execute update sql error");
        }
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS authentication (
        authToken varchar(256) NOT NULL,
        username varchar(256) NOT NULL,
        json TEXT DEFAULT NULL,
        PRIMARY KEY (username),
        INDEX(authToken)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database");
        }
    }
}
