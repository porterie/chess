package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int gameiD = 0;
    public Integer createGame(String gameName) throws DataAccessException {
        gameiD++;
        games.put(gameiD, new GameData(gameiD, null, null, gameName, new ChessGame()));
        return gameiD;
    }

    public void clear() throws DataAccessException {
        games.clear();
    }

    public GameData getGame(int iD) throws DataAccessException {
        return games.get(iD);
    }

    public void updateGame(int iD, GameData game) throws DataAccessException {
        games.put(iD, game);
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }
}
