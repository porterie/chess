package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    void clear() throws DataAccessException;
    GameData getGame(int ID) throws DataAccessException;
    void updateGame(int ID, GameData game) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
}
