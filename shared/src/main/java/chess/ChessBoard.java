package chess;

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
        squares[position.getRow()][position.getColumn()] = piece;

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
        return squares[position.getRow()][position.getColumn()];
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
        ChessPosition bRook1Pos = new ChessPosition(7, 0);
        ChessPosition bRook2Pos = new ChessPosition(7,7);
        addPiece(bRook1Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(bRook2Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        //knights
        ChessPosition bKnight1Pos = new ChessPosition(7, 1);
        ChessPosition bKnight2Pos = new ChessPosition(7, 6);
        addPiece(bKnight1Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(bKnight2Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        //obispos
        ChessPosition bBishop1Pos = new ChessPosition(7, 2);
        ChessPosition bBishop2Pos = new ChessPosition(7, 5);
        addPiece(bBishop1Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(bBishop2Pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        //queen
        ChessPosition bQueenPos = new ChessPosition(7, 3);
        addPiece(bQueenPos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        //king
        ChessPosition bKingPos = new ChessPosition(7, 4);
        addPiece(bKingPos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //sets black pawns
        for (int j = 0; j < 8; j++){
            ChessPosition pawnPos = new ChessPosition(j, 6);
            addPiece(pawnPos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }

        //WHITE backline
        //rooks
        ChessPosition wRook1Pos = new ChessPosition(0, 0);
        ChessPosition wRook2Pos = new ChessPosition(0,7);
        addPiece(wRook1Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(wRook2Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        //knights
        ChessPosition wKnight1Pos = new ChessPosition(0, 1);
        ChessPosition wKnight2Pos = new ChessPosition(0, 6);
        addPiece(wKnight1Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(wKnight2Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        //obispos
        ChessPosition wBishop1Pos = new ChessPosition(0, 2);
        ChessPosition wBishop2Pos = new ChessPosition(0, 5);
        addPiece(wBishop1Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(wBishop2Pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //queen
        ChessPosition wQueenPos = new ChessPosition(0, 3);
        addPiece(wQueenPos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        //king
        ChessPosition wKingPos = new ChessPosition(0, 4);
        addPiece(wKingPos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        //white pawns
        //sets black pawns
        for (int j = 0; j < 8; j++){
            ChessPosition pawnPos = new ChessPosition(j, 1);
            addPiece(pawnPos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }


        //throw new RuntimeException("Not implemented");
    }
}
