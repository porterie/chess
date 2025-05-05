package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece; //-1's are because getRow and getCol  must return in 1-8 indexing, while board operations require 0-7

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1]; //-1's are because getRow and getCol  must return in 1-8 indexing, while board operations require 0-7
        //throw new RuntimeException("Not implemented");
    }

    public Boolean isEmpty(ChessPosition position) {
        return getPiece(position) == null;
    }



    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //sets everything to null
        squares = new ChessPiece[8][8];

        //sets black backline

        //rooks
        ChessPosition bRook1Pos = new ChessPosition(8, 1);
        ChessPosition bRook2Pos = new ChessPosition(8,8);
        addPiece(bRook1Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(bRook2Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        //knights
        ChessPosition bKnight1Pos = new ChessPosition(8, 2);
        ChessPosition bKnight2Pos = new ChessPosition(8, 7);
        addPiece(bKnight1Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(bKnight2Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        //obispos
        ChessPosition bBishop1Pos = new ChessPosition(8, 3);
        ChessPosition bBishop2Pos = new ChessPosition(8, 6);
        addPiece(bBishop1Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(bBishop2Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        //queen
        ChessPosition bQueenPos = new ChessPosition(8, 4);
        addPiece(bQueenPos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        //king
        ChessPosition bKingPos = new ChessPosition(8, 5);
        addPiece(bKingPos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //sets black pawns
        for (int j = 1; j <= 8; j++){
            ChessPosition pawnPos = new ChessPosition(j, 7);
            addPiece(pawnPos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        //WHITE backline
        //rooks
        ChessPosition wRook1Pos = new ChessPosition(1, 1);
        ChessPosition wRook2Pos = new ChessPosition(1,8);
        addPiece(wRook1Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(wRook2Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //knights
        ChessPosition wKnight1Pos = new ChessPosition(1, 2);
        ChessPosition wKnight2Pos = new ChessPosition(1, 7);
        addPiece(wKnight1Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(wKnight2Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        //obispos
        ChessPosition wBishop1Pos = new ChessPosition(1, 3);
        ChessPosition wBishop2Pos = new ChessPosition(1, 6);
        addPiece(wBishop1Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(wBishop2Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //queen
        ChessPosition wQueenPos = new ChessPosition(1, 4);
        addPiece(wQueenPos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //king
        ChessPosition wKingPos = new ChessPosition(1, 5);
        addPiece(wKingPos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //white pawns
        //sets white pawns
        for (int j = 1; j <= 8; j++){
            ChessPosition pawnPos = new ChessPosition(j, 2);
            addPiece(pawnPos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }


        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
