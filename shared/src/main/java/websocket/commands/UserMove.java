package websocket.commands;

import chess.ChessMove;

public class UserMove extends UserGameCommand {

    ChessMove move;
    CommandType type;
    public UserMove(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
        super(commandType, authToken, gameID);
        this.move = move;
        type = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {return move;}
}
