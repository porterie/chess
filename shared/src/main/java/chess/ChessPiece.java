package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        PieceType piece = getPieceType();
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        //Movement logic for bishop
        if(piece==PieceType.BISHOP){
            //look NorthWest
            for(int i = col; i >= 0; i--){
                for(int j = row; j < 8; j++){
                    //check if space is occupied by a piece
                    ChessPosition pos_temp = new ChessPosition(j,i);
                    if(board.isEmpty(pos_temp)){
                        moves.add(new ChessMove(myPosition, pos_temp, piece));
                    }
                }
            }
            //look NorthEast
            for(int i = col; i < 8; i++){
                for(int j = row; j < 8; j++){
                    //check if space is occupied by a piece
                    ChessPosition pos_temp = new ChessPosition(j,i);
                    if(board.isEmpty(pos_temp)){
                        moves.add(new ChessMove(myPosition, pos_temp, piece));
                    }
                }
            }
            //look SouthWest
            for(int i = col; i >= 0; i--){
                for(int j = row; j >= 0; j--){
                    //check if space is occupied by a piece
                    ChessPosition pos_temp = new ChessPosition(j,i);
                    if(board.isEmpty(pos_temp)){
                        moves.add(new ChessMove(myPosition, pos_temp, piece));
                    }
                }
            }
            //look SouthEast
            for(int i = col; i < 8; i++){
                for(int j = row; j >= 0; j--){
                    //check if space is occupied by a piece
                    ChessPosition pos_temp = new ChessPosition(j,i);
                    if(board.isEmpty(pos_temp)){
                        moves.add(new ChessMove(myPosition, pos_temp, piece));
                    }
                }
            }
        }

        return moves;
        //throw new RuntimeException("Not implemented");
    }
}
