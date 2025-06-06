package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Types.NULL;

public class MySqlGameDAO implements GameDAO {

    public MySqlGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException exception) {
            throw new RuntimeException(" MySqlGameDAO init problem", exception);
        }
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        //game with only name to create id
        var statement = "INSERT INTO game (gameName) VALUES (?)";

        int gameID = -1; //init to inviable ID. Should change later in function if works
        try(var conn = DatabaseManager.getConnection()){
            //var statement2 = "SELECT gameID FROM game WHERE gameName=?";
            try(var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, gameName);
                ps.executeUpdate();
                try(var rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        gameID = rs.getInt(1);
                    }else{
                        System.out.println("createGame id does not return key");
                    }
                }
            }
            //using ID add GameData
            GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
            var json = new Gson().toJson(gameData);
            var statement2 = "UPDATE game SET json=? WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement2)){
                ps.setString(1, json);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        }catch(SQLException exception){
            throw new DataAccessException("Error: Database access exception createGame", exception);
        }
        return gameID;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    @Override
    public GameData getGame(int iD) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT json FROM game WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1, iD);
                try(var rs = ps.executeQuery()){
                    if(rs.next()){
                        var gameJson = rs.getString("json");
                        return new Gson().fromJson(gameJson, GameData.class);
                    }
                }
            }
        }catch(SQLException exception){
            throw new DataAccessException("Error: Database access exception getGame");
        }
        System.out.println("ALERT: getGame returning null.");
        return null;
    }

    @Override
    public void updateGame(int iD, GameData game) throws DataAccessException {
        var statement = "UPDATE game SET json=? WHERE gameID=?";
        var newGameJson = new Gson().toJson(game);
        executeUpdate(statement, newGameJson, iD);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        //see listPets() for implementation reference (without readPets stuff)
        Collection<GameData> gameList = new ArrayList<GameData>();

        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT json FROM game";
            try (var ps = conn.prepareStatement(statement)){
                try(var rs = ps.executeQuery()){
                    while (rs.next()){
                        GameData currentGame = new Gson().fromJson(rs.getString("json"), GameData.class);
                        gameList.add(currentGame);
                    }
                }
            }
        }catch(SQLException exception){
            throw new DataAccessException("Error: problem in listGames");
        }
        return gameList;
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
            throw new DataAccessException("Error: execute update sql doesn't work. Likely cause in calling function", exception);
        }
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS game (
        gameID INT NOT NULL AUTO_INCREMENT,
        whiteUsername varchar(256) DEFAULT NULL,
        blackUsername varchar(256) DEFAULT NULL,
        gameName varchar(256) NOT NULL,
        json TEXT DEFAULT NULL,
        PRIMARY KEY (gameID)
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
            throw new DataAccessException("Error: Unable to configure game database");
        }
    }
}
