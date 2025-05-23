package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 0;
    public Integer createGame(String gameName) throws DataAccessException {
        gameID++;
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return gameID;
    }

    public void clear() throws DataAccessException {
        games.clear();
    }

    public GameData getGame(int ID) throws DataAccessException {
        return games.get(ID);
    }

    public void updateGame(int ID, GameData game) throws DataAccessException {
        games.put(ID, game);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }
}
