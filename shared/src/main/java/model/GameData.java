package model;

import chess.ChessGame;

import java.util.Objects;

public class GameData {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;
    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        this.gameID = gameID;
        this.game = game;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getWhiteUsername() {
        return whiteUsername;

    }

    public void setWhiteUsername(String name){
        whiteUsername = name;
    }
    public void setBlackUsername(String name){
        blackUsername = name;
    }

}
