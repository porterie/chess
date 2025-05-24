package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.AuthDAO;
import model.GameData;


import java.util.Collection;
import java.util.Objects;

public class GameService {
    private GameDAO gameDAO = new MemoryGameDAO();
    public Boolean whiteFree(Integer gameID) throws DataAccessException {
        return Objects.equals(gameDAO.getGame(gameID).getWhiteUsername(), null);
    }
    public Boolean blackFree(Integer gameID) throws DataAccessException {
        return Objects.equals(gameDAO.getGame(gameID).getBlackUsername(), null);
    }
    public void clearMemory() throws DataAccessException{
        gameDAO = new MemoryGameDAO();
    }
    public String listGames() throws DataAccessException {
       //Constructs the JSON list of games
        String gameList = "{ \"games\": [";
        Collection<GameData> games = gameDAO.listGames();

        for (GameData game : games) {
            String whiteUsername = game.getWhiteUsername();
            String blackUsername = game.getBlackUsername();
            gameList = gameList + " {\"gameID\": " + game.getGameID() + ", \"whiteUsername\":"
                    + (whiteUsername != null ? "\"" + whiteUsername + "\"" : null) +
                    ", \"blackUsername\":" + (blackUsername != null ? "\"" + blackUsername + "\"" : null)
                    + ", \"gameName\":\"" + game.getGameName() + "\"},";
        }
        if(!games.isEmpty())
            gameList = gameList.substring(0, gameList.length()-1);

        gameList = gameList + "] }";

        System.out.print(gameList);
        return gameList;

        //return new ListGamesResult(gameDAO.listGames());
    }
    public CreateGameResult createGame(String gameName) throws DataAccessException {
        if(gameName!=null){
        return new CreateGameResult(gameDAO.createGame(gameName));
        }else{
            return null;
        }
    }
    public Boolean validGame(Integer gameID) throws DataAccessException {
        if(gameID!=null && gameDAO.getGame(gameID)!=null){
            return true;
        }else {return false;}
    }
    public void joinGame(String playerColor, Integer gameID, String playerName) throws DataAccessException {
        GameData game = gameDAO.getGame(gameID);
        if(Objects.equals(playerColor, "WHITE")){
            System.out.println("setting white user");
            game.setWhiteUsername(playerName);
        }else if(Objects.equals(playerColor, "BLACK")){
            System.out.println("setting black user");
            game.setBlackUsername(playerName);
        }else{
            System.out.println("Color field incorrect");
            System.out.println(playerColor);
        }
        gameDAO.updateGame(gameID, game);
    }

}
