package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.SQLException;


public class MySqlUserDAO implements UserDAO{
    //see Petshop MySqlDataAccess.java example
    public MySqlUserDAO() {

    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }
    private int executeUpdate(String statement, Object... params) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){

        }catch(SQLException exception){
            throw new DataAccessException("execute update sql error");
        }
    }
    private final String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS user (
        'username' varchar(256) NOT NULL,
        'password' varchar(256) NOT NULL,
        'email' varchar(256) NOT NULL,
        'json' TEXT DEFAULT NULL,
        PRIMARY KEY ('username'),
        INDEX('username')
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        var conn = DatabaseManager.getConnection();
        for (var statement : createStatements) {
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }catch(SQLException ex){
                throw new DataAccessException("Unable to configure database");
            }
        }
    }
}
