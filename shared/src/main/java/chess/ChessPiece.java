package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
       this.pieceColor = pieceColor;
       this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;

        //throw new RuntimeException("Not implemented");
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
        if(piece==PieceType.BISHOP) {
            //look NorthEast
            for (int i = 1; (col + i <=8) && (row + i <= 8); i++) {
                //check if space is occupied by a piece
                ChessPosition pos_temp = new ChessPosition(row + i, col + i);
                if (board.isEmpty(pos_temp)) {
                    moves.add(new ChessMove(myPosition, pos_temp, piece));
                    System.out.printf("Adding move to available space: %d, %d%n", row + i, col + i); //todo: remove debug
                }
            }
            //look Southeast
            for (int i = 1; (col + i <= 8) && (row - i >= 1); i++) {
                //check if space is occupied by a piece
                ChessPosition pos_temp = new ChessPosition(row - i, col + i);
                if (board.isEmpty(pos_temp)) {
                    moves.add(new ChessMove(myPosition, pos_temp, piece));
                    System.out.printf("Adding move to available space: %d, %d%n", row - i, col + i); //todo: remove debug
                }
            }
            //look SouthWest
            for (int i = 1; (col - i >= 1) && (row - i >= 1); i++) {
                //check if space is occupied by a piece
                ChessPosition pos_temp = new ChessPosition(row - i, col - i);
                if (board.isEmpty(pos_temp)) {
                    moves.add(new ChessMove(myPosition, pos_temp, piece));
                    System.out.printf("Adding move to available space: %d, %d%n", row - i, col - i); //todo: remove debug
                }
            }
            //look NorthWest
            for (int i = 1; (col - i >= 1) && (row + i <= 8); i++) {
                //check if space is occupied by a piece
                ChessPosition pos_temp = new ChessPosition(row + i, col - i);
                if (board.isEmpty(pos_temp)) {
                    moves.add(new ChessMove(myPosition, pos_temp, piece));
                    System.out.printf("Adding move to available space: %d, %d%n", row + i, col - i); //todo: remove debug
                }
            }
        }
        return moves;
        //throw new RuntimeException("Not implemented");
    }
}
