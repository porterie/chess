package dataaccess;

import model.GameData;

import javax.xml.crypto.Data;

public interface GameDAO {
    void createGame() throws DataAccessException;
    void clear() throws DataAccessException;
    GameData getGame() throws DataAccessException;
}
