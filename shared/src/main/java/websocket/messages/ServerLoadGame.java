package websocket.messages;

import chess.ChessGame;

public class ServerLoadGame extends ServerMessage {
    ChessGame game;
    ServerMessageType serverMessageType;
    public ServerLoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
        serverMessageType = ServerMessageType.LOAD_GAME;
    }

    public ChessGame getGame() {return game;}
}
