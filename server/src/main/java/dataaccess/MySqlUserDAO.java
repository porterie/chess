package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;



public class MySqlUserDAO implements UserDAO{



    //see Petshop MySqlDataAccess.java example
    public MySqlUserDAO() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(user);
        executeUpdate(statement, user.getUsername(), user.getPasswd(), user.getEmail(), json);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT json FROM user WHERE username=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(var rs = ps.executeQuery()){
                    if(rs.next()){
                        var userJson = rs.getString("json");
                        return new Gson().fromJson(userJson, UserData.class);
                    }
                }
            }
        }catch(SQLException exception){
            throw new DataAccessException("Error: Database access exception getUser");
        }
        System.out.println("ALERT: getUser returning null.");
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {
        var statement = "DELETE FROM user WHERE username=?";
        executeUpdate(statement, username);
    }
    private void executeUpdate(String statement, Object... params) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if(param instanceof Integer p) {ps.setInt(i+1, p);}
                    else if(param instanceof Boolean p) {ps.setBoolean(i+1, p);}
                    else if(param == null){ps.setNull(i+1, java.sql.Types.NULL);}
                }
                ps.executeUpdate();

            }
        }catch (SQLException exception) {
            throw new DataAccessException("Error: execute update sql. Likely cause in calling function", exception);
        }
    }
    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS user (
        username varchar(256) NOT NULL,
        password varchar(256) NOT NULL,
        email varchar(256) NOT NULL,
        json TEXT DEFAULT NULL,
        PRIMARY KEY (username)
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
            throw new DataAccessException("Error: Unable to configure database");
        }
    }
}
