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

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
                ChessPosition posTemp = new ChessPosition(row + i, col + i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look Southeast
            for (int i = 1; (col + i <= 8) && (row - i >= 1); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row - i, col + i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d%n", row - i, col + i);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look SouthWest
            for (int i = 1; (col - i >= 1) && (row - i >= 1); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row - i, col - i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d%n", row - i, col - i);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look NorthWest
            for (int i = 1; (col - i >= 1) && (row + i <= 8); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row + i, col - i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d%n", row + i, col - i);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
        }else if(piece==PieceType.KING){ //KING
            //North
            if(row+1 <= 8){
                ChessPosition posTemp = new ChessPosition(row+1, col);
                if (board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }//no blocked case neccessary
            }
            //Northeast
            if(row+1 <= 8 && col+1 <= 8){
                ChessPosition posTemp = new ChessPosition(row+1, col+1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //East
            if(col + 1 <= 8){
                ChessPosition posTemp = new ChessPosition(row, col+1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //Southeast
            if(col + 1 <=8 && row - 1 >= 1){
                ChessPosition posTemp = new ChessPosition(row-1, col+1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //South
            if(row - 1 >= 1){
                ChessPosition posTemp = new ChessPosition(row-1, col);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //Southwest
            if(row-1 >= 1 && col-1 >= 1){
                ChessPosition posTemp = new ChessPosition(row-1, col-1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //West
            if(col -1 >= 1){
                ChessPosition posTemp = new ChessPosition(row, col-1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //Northwest
            if(col-1 >= 1 && row + 1 <= 8){
                ChessPosition posTemp = new ChessPosition(row+1, col-1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
        }else if(piece==PieceType.KNIGHT){
            //North northeast
            if(row+2 <= 8 && col +1 <= 8){
                ChessPosition posTemp = new ChessPosition(row+2, col+1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //East northeast
            if(row+1 <= 8 && col + 2 <= 8){
                ChessPosition posTemp = new ChessPosition(row+1, col+2);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            // East Southeast
            if(row-1 >= 1 && col + 2 <= 8){
                ChessPosition posTemp = new ChessPosition(row-1, col+2);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //South southeast
            if(row-2 >= 1 && col+1 <=8){
                ChessPosition posTemp = new ChessPosition(row-2, col+1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //South southwest
            if(row-2 >= 1 && col-1 >= 1){
                ChessPosition posTemp = new ChessPosition(row-2, col-1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //West southwest
            if(row-1 >= 1 && col-2 >= 1){
                ChessPosition posTemp = new ChessPosition(row-1, col-2);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //West Northwest
            if(row+1 <= 8 && col -2 >= 1){
                ChessPosition posTemp = new ChessPosition(row+1, col-2);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
            //North northwest
            if(row+2 <= 8 && col -1 >= 1){
                ChessPosition posTemp = new ChessPosition(row+2, col-1);
                if(board.isEmpty(posTemp)){
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }else if (board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                }
            }
        }else if(piece==PieceType.ROOK){
            //North
            for(int i = 1; i+row <= 8; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row + i, col);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //East
            for(int i = 1; i +col <= 8; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row, col + i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //South
            for(int i = 1; row-i >= 1; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row - i, col);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //West
            for(int i = 1; col-i >= 1; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row, col - i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
        }else if(piece==PieceType.QUEEN){
            //added bishop and rook logic.
            //North
            for(int i = 1; i+row <= 8; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row + i, col);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //East
            for(int i = 1; i +col <= 8; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row, col + i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //South
            for(int i = 1; row-i >= 1; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row - i, col);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //West
            for(int i = 1; col-i >= 1; i++){
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row, col - i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look NorthEast
            for (int i = 1; (col + i <=8) && (row + i <= 8); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row + i, col + i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d from %d, %d%n", row + i, col + i, row, col);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look Southeast
            for (int i = 1; (col + i <= 8) && (row - i >= 1); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row - i, col + i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d%n", row - i, col + i);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look SouthWest
            for (int i = 1; (col - i >= 1) && (row - i >= 1); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row - i, col - i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d%n", row - i, col - i);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
            //look NorthWest
            for (int i = 1; (col - i >= 1) && (row + i <= 8); i++) {
                //check if space is occupied by a piece
                ChessPosition posTemp = new ChessPosition(row + i, col - i);
                if (board.isEmpty(posTemp)) {
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    //System.out.printf("Adding move to available space: %d, %d%n", row + i, col - i);
                }else if(board.getPiece(posTemp).getTeamColor() != this.getTeamColor()){
                    //capture case
                    moves.add(new ChessMove(myPosition, posTemp, null));
                    break;
                }else if(board.getPiece(posTemp).getTeamColor() == this.getTeamColor()){
                    //blocked case
                    break;
                }
            }
        }else if(piece == PieceType.PAWN){
            //check color for directionality
            if(pieceColor== ChessGame.TeamColor.WHITE){
                //charge
                if(row==2){
                    ChessPosition posTemp = new ChessPosition(row+2, col);
                    ChessPosition posTemp2 = new ChessPosition(row+1, col);

                    if(board.isEmpty(posTemp) && board.isEmpty(posTemp2)){
                        moves.add(new ChessMove(myPosition, posTemp, null));
                    }
                }
                //advance
                ChessPosition posTemp = new ChessPosition(row + 1, col);
                if (board.isEmpty(posTemp)){
                    if(row+1==8){
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.ROOK));
                    }else {
                        moves.add(new ChessMove(myPosition, posTemp, null));
                    }
                }
                //look left
                posTemp = new ChessPosition(row+1, col -1);
                if(col-1>=1) {
                    if (!board.isEmpty(posTemp)) {
                        if (board.getPiece(posTemp).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            if (row + 1 == 8) {
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.QUEEN));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.KNIGHT));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.ROOK));
                            } else {
                                moves.add(new ChessMove(myPosition, posTemp, null));
                            }
                        }
                    }
                }
                //look right
                posTemp = new ChessPosition(row+1, col +1);
                if(col+1<=8) {
                    if (!board.isEmpty(posTemp)) {
                        if (board.getPiece(posTemp).getTeamColor() == ChessGame.TeamColor.BLACK) {
                            if (row + 1 == 8) {
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.QUEEN));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.KNIGHT));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.ROOK));
                            } else {
                                moves.add(new ChessMove(myPosition, posTemp, null));
                            }
                        }
                    }
                }
            }else{//black
                //charge
                if(row==7){
                    ChessPosition posTemp = new ChessPosition(row-2, col);
                    ChessPosition posTemp2 = new ChessPosition(row-1, col);
                    if(board.isEmpty(posTemp) && board.isEmpty(posTemp2)){
                        moves.add(new ChessMove(myPosition, posTemp, null));
                    }
                }
                //advance
                ChessPosition posTemp = new ChessPosition(row - 1, col);
                if (board.isEmpty(posTemp)){
                    if(row-1==1){
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, posTemp, PieceType.ROOK));
                    }else {
                        moves.add(new ChessMove(myPosition, posTemp, null));
                    }
                }
                //look left
                posTemp = new ChessPosition(row-1, col -1);
                if(col-1>=1) {
                    if (!board.isEmpty(posTemp)) {
                        if (board.getPiece(posTemp).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            if (row - 1 == 1) {
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.QUEEN));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.KNIGHT));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.ROOK));
                            } else {
                                moves.add(new ChessMove(myPosition, posTemp, null));
                            }
                        }
                    }
                }
                //look right
                posTemp = new ChessPosition(row-1, col +1);
                if(col+1<=8) {
                    if (!board.isEmpty(posTemp)) {
                        if (board.getPiece(posTemp).getTeamColor() == ChessGame.TeamColor.WHITE) {
                            if (row - 1 == 1) {
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.QUEEN));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.BISHOP));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.KNIGHT));
                                moves.add(new ChessMove(myPosition, posTemp, PieceType.ROOK));
                            } else {
                                moves.add(new ChessMove(myPosition, posTemp, null));
                            }
                        }
                    }
                }
            }
        }
        return moves;
        //throw new RuntimeException("Not implemented");
    }
}
