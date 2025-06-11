package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

public class ServerLoadGame extends ServerMessage {
    ChessGame game;
    ServerMessageType serverMessageType;
    PlayerType playerType;
    public enum PlayerType {
        WHITE,
        BLACK,
        OBSERVER
    }
    public ServerLoadGame(ServerMessageType type, ChessGame game, String playerType) {
        super(type);
        this.game = game;
        serverMessageType = ServerMessageType.LOAD_GAME;
        if(Objects.equals(playerType, "WHITE")){
            this.playerType=PlayerType.WHITE;
        }else if(Objects.equals(playerType, "BLACK")){
            this.playerType=PlayerType.BLACK;
        }else{
            this.playerType=PlayerType.OBSERVER;
        }
    }

    public PlayerType getPlayerType(){return playerType;}

    public ChessGame getGame() {return game;}
}
