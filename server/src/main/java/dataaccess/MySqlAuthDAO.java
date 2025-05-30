package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO {

    public MySqlAuthDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException exception) {
            throw new RuntimeException("ERROR: MySqlAuthDAO init problem", exception);
        }
    }

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
            throw new DataAccessException("Error: Database access exception getAuthTokens");
        }
        System.out.println("ALERT: getAuthTokens returning null.");
        return null;
    }

    @Override
    public void deleteAuthTokens(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authentication WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void createAuthTokens(String authToken, String username) throws DataAccessException {
        var statement = "INSERT INTO authentication (authToken, username, json) VALUES (?, ?, ?)";
        AuthData authData = new AuthData(authToken, username);
        var json = new Gson().toJson(authData);
        executeUpdate(statement, authToken, username, json);
        System.out.println("Creating authToken: " + authToken + " for user: " + username);
        printAuthTableSchema();
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
            throw new DataAccessException("Error: Database access exception getAuthUser");
        }
        System.out.println("ALERT: getAuthUser returning null.");
        return null;
    }

    @Override
    public Boolean isAuthToken(String authToken) throws DataAccessException {
        return getAuthUser(authToken) != null;
    }
    public void clear(){
        var statement = "TRUNCATE authentication";
        try {
            executeUpdate(statement);
        } catch (DataAccessException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()) {
            if(params.length==0){
                try(var clearStatement = conn.createStatement()){
                    System.out.println("Authentication clear");
                    clearStatement.executeUpdate(statement);
                }
            }else {
                try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                    for (var i = 0; i < params.length; i++) {
                        var param = params[i];
                        if (param instanceof String p) {
                            ps.setString(i + 1, p);
                        } else if (param instanceof Integer p) {
                            ps.setInt(i + 1, p);
                        } else if (param instanceof Boolean p) {
                            ps.setBoolean(i + 1, p);
                        } else if (param == null) {
                            ps.setNull(i + 1, java.sql.Types.NULL);
                        }
                    }
                    ps.executeUpdate();

                }
            }
        }catch (SQLException exception) {
            exception.printStackTrace();
            throw new DataAccessException("Error: execute update sql, likely cause in calling function", exception);
        }
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS authentication (
        authToken varchar(256) NOT NULL,
        username varchar(256) NOT NULL,
        json TEXT DEFAULT NULL,
        PRIMARY KEY (authToken)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };
    private void configureDatabase() throws DataAccessException {

        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
             /*
            try (var dropStmt = conn.createStatement()) {
                dropStmt.executeUpdate("DROP TABLE IF EXISTS authentication");
                System.out.println("Authentication table dropped and will be recreated.");
            }*/
            //code above resets database schema. remove before submission

            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: Unable to configure database");
        }
    }

    public void printAuthTableSchema() {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement("SHOW CREATE TABLE authentication");
             var rs = stmt.executeQuery()) {

            if (rs.next()) {
                String tableName = rs.getString(1);
                String createSQL = rs.getString(2);
                System.out.println("Table: " + tableName);
                System.out.println(createSQL);
            }

        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        }
    }

}
