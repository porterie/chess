package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.AuthDAO;
import model.GameData;


import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO = new MemoryGameDAO();
    ListGamesResult listGames() throws DataAccessException {
       /* //Constructs the JSON list of games
        String gameList = "\"games\":";
        Collection<GameData> games = gameDAO.listGames();
        for (GameData game : games) {
            gameList = gameList + " [{\"gameID\": " + game.getGameID() + ", \"whiteUsername\":\""
                    + game.getWhiteUsername() + "\", \"blackUsername\":\"" + game.getBlackUsername()
                    + "\", \"gameName\":\"" + game.getGameName() + "\"} ],";
        }
        gameList = gameList.substring(0, gameList.length()-1);
        gameList = gameList + "}";

        return gameList;*/

        return new ListGamesResult(gameDAO.listGames());
    }
    CreateGameResult createGame(String gameName) throws DataAccessException {
        return new CreateGameResult(gameDAO.createGame(gameName));
    }
    void joinGame(String playerColor, Integer gameID, String playerName) throws DataAccessException {
        GameData game = gameDAO.getGame(gameID);
        if(Objects.equals(playerColor, "WHITE")){
            game.setWhiteUsername(playerName);
        }else if(Objects.equals(playerColor, "BLACK")){
            game.setBlackUsername(playerName);
        }
        gameDAO.updateGame(gameID, game);
    }

}
