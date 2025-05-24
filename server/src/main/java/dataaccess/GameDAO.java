package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    void clear() throws DataAccessException;
    GameData getGame(int iD) throws DataAccessException;
    void updateGame(int iD, GameData game) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
}
