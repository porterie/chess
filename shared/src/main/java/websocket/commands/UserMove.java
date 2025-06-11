package websocket.commands;

import chess.ChessMove;

public class UserMove extends UserGameCommand {

    ChessMove move;
    CommandType commandType;
    public UserMove(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {return move;}
}
