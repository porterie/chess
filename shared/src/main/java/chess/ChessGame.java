package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTeam; //initialize team to white
    private ChessBoard gameBoard = new ChessBoard();
    public ChessGame() {
        gameBoard.resetBoard();
        currentTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    public ChessPosition findKing(TeamColor color){
        ChessPosition kingPos = null;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition pos = new ChessPosition(j,i);
                if (gameBoard.getPiece(pos)!=null && gameBoard.getPiece(pos).getPieceType()== ChessPiece.PieceType.KING && gameBoard.getPiece(pos).getTeamColor()==color){
                    kingPos = pos;
                }
            }
        }
        return kingPos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTeam == chessGame.currentTeam && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeam, gameBoard);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "currentTeam=" + currentTeam +
                ", gameBoard=" + gameBoard +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //throw new RuntimeException("Not implemented");
        ChessPiece gamePiece = gameBoard.getPiece(startPosition);
        ChessBoard testBoard = gameBoard;
        if(gamePiece!=null) {
            Collection<ChessMove> moves = gamePiece.pieceMoves(gameBoard, startPosition);
            Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
                for(ChessMove move : moves){
                    ChessPiece movePiece = gameBoard.getPiece(move.getStartPosition());
                    ChessPiece tempPiece = gameBoard.getPiece(move.getEndPosition());
                    gameBoard.addPiece(move.getStartPosition(),null);
                    gameBoard.addPiece(move.getEndPosition(), movePiece);
                    if(!isInCheck(gamePiece.getTeamColor())) {
                        validMoves.add(move);
                        System.out.print("Adding move: ");
                        System.out.print(move);
                    }
                    //return pieces
                    gameBoard.addPiece(move.getStartPosition(),movePiece);
                    gameBoard.addPiece(move.getEndPosition(),tempPiece);

                }
            return validMoves;
        } else{
            return Collections.emptyList();
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw new RuntimeException("Not implemented");
        if (gameBoard.getPiece(move.getStartPosition())!=null && gameBoard.getPiece(move.getStartPosition()).getTeamColor()==currentTeam && validMoves(move.getStartPosition()).contains(move)){
            //move valid
            if(move.getPromotionPiece()==null) {
                ChessPiece movePiece = gameBoard.getPiece(move.getStartPosition());
                gameBoard.addPiece(move.getStartPosition(), null);
                gameBoard.addPiece(move.getEndPosition(), movePiece);
            }else{
                ChessPiece promotedPiece = new ChessPiece(gameBoard.getPiece((move.getStartPosition())).getTeamColor(), move.getPromotionPiece());
                gameBoard.addPiece(move.getEndPosition(), promotedPiece);
                gameBoard.addPiece(move.getStartPosition(), null);
            }
            if(currentTeam==TeamColor.WHITE){
                currentTeam=TeamColor.BLACK;
            }else{
                currentTeam=TeamColor.WHITE;
            }
        }else{
            //move invalid
            throw new InvalidMoveException("Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false; //initialize to false
        ChessPosition myPosition = findKing(teamColor);
        //Can only be captured from one of the cardinal directions, diagonals, or knight movement.

        //BORROWED FROM QUEEN MOVEMENT LOGIC
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        //North
        for(int i = 1; i+row <= 8; i++){//cardinal direction check logic
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row + i, col);
            if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                //capture case
                if(i==1){
                    //king
                    if(gameBoard.getPiece(pos_temp).getPieceType() == (ChessPiece.PieceType.KING)){
                        check = true;
                    }
                }
               if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                   check = true;
               if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.ROOK)
                   check = true;
               break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //East
        for(int i = 1; i +col <= 8; i++){
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row, i + col);
            if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                //capture case
                if(i==1){
                    //king
                    if(gameBoard.getPiece(pos_temp).getPieceType() == (ChessPiece.PieceType.KING)){
                        check = true;
                    }
                }
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.ROOK)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //South
        for(int i = 1; row-i >= 1; i++){
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row - i, col);
            if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                //capture case
                if(i==1){
                    //king
                    if(gameBoard.getPiece(pos_temp).getPieceType() == (ChessPiece.PieceType.KING)){
                        check = true;
                    }
                }
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.ROOK)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //West
        for(int i = 1; col-i >= 1; i++){
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row, col-i);
            if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                //capture case
                if(i==1){
                    //king
                    if(gameBoard.getPiece(pos_temp).getPieceType() == (ChessPiece.PieceType.KING)){
                        check = true;
                    }
                }
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.ROOK)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //look NorthEast
        for (int i = 1; (col + i <=8) && (row + i <= 8); i++) {//check logic for diagonal
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row + i, col + i);
            if(!gameBoard.isEmpty(pos_temp) && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(i == 1){//pawn and king
                    if(teamColor==TeamColor.WHITE)
                        if(gameBoard.getPiece(pos_temp).getPieceType()==ChessPiece.PieceType.PAWN)
                            check = true;
                    if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KING)
                        check = true;
                }
                //bishop, queen
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.BISHOP)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //look Southeast
        for (int i = 1; (col + i <= 8) && (row - i >= 1); i++) {
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row - i, col + i);
            if(!gameBoard.isEmpty(pos_temp) && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(i == 1){//pawn and king
                    if(teamColor==TeamColor.BLACK)
                        if(gameBoard.getPiece(pos_temp).getPieceType()==ChessPiece.PieceType.PAWN)
                            check = true;
                    if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KING)
                        check = true;
                }
                //bishop, queen
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.BISHOP)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //look SouthWest
        for (int i = 1; (col - i >= 1) && (row - i >= 1); i++) {
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row - i, col - i);
            if(!gameBoard.isEmpty(pos_temp) && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(i == 1){//pawn and king
                    if(teamColor==TeamColor.BLACK)
                        if(gameBoard.getPiece(pos_temp).getPieceType()==ChessPiece.PieceType.PAWN)
                            check = true;
                    if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KING)
                        check = true;
                }
                //bishop, queen
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.BISHOP)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }
        //look NorthWest
        for (int i = 1; (col - i >= 1) && (row + i <= 8); i++) {
            //check if space is occupied by a piece
            ChessPosition pos_temp = new ChessPosition(row + i, col - i);
            if(!gameBoard.isEmpty(pos_temp) && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(i == 1){//pawn and king
                    if(teamColor==TeamColor.WHITE)
                        if(gameBoard.getPiece(pos_temp).getPieceType()==ChessPiece.PieceType.PAWN)
                            check = true;
                    if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KING)
                        check = true;
                }
                //bishop, queen
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.QUEEN)
                    check = true;
                if(gameBoard.getPiece(pos_temp).getPieceType() == ChessPiece.PieceType.BISHOP)
                    check = true;
                break;
            }else if(gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() == teamColor){
                //blocked case
                break;
            }
        }

        //NOW LOGIC BORROWED FROM KNIGHT MOVEMENT
        //North northeast
        if(row+2 <= 8 && col +1 <= 8){
            ChessPosition pos_temp = new ChessPosition(row+2, col+1);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        //East northeast
        if(row+1 <= 8 && col + 2 <= 8){
            ChessPosition pos_temp = new ChessPosition(row+1, col+2);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        // East Southeast
        if(row-1 >= 1 && col + 2 <= 8){
            ChessPosition pos_temp = new ChessPosition(row-1, col+2);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        //South southeast
        if(row-2 >= 1 && col+1 <=8){
            ChessPosition pos_temp = new ChessPosition(row-2, col+1);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        //South southwest
        if(row-2 >= 1 && col-1 >= 1){
            ChessPosition pos_temp = new ChessPosition(row-2, col-1);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        //West southwest
        if(row-1 >= 1 && col-2 >= 1){
            ChessPosition pos_temp = new ChessPosition(row-1, col-2);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        //West Northwest
        if(row+1 <= 8 && col -2 >= 1){
            ChessPosition pos_temp = new ChessPosition(row+1, col-2);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        //North northwest
        if(row+2 <= 8 && col -1 >= 1){
            ChessPosition pos_temp = new ChessPosition(row+2, col-1);
            if (gameBoard.getPiece(pos_temp) != null && gameBoard.getPiece(pos_temp).getTeamColor() != teamColor){
                if(gameBoard.getPiece(pos_temp).getPieceType()== ChessPiece.PieceType.KNIGHT)
                    check = true;
            }
        }
        return check;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean checkmate = false; //init to false
        ChessPosition kingPos = findKing(teamColor);
        if(isInCheck(teamColor)){//king is in check where it is
         if(validMoves(kingPos).isEmpty())
             checkmate = true;
        }
        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = true;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition temp = new ChessPosition(j,i);
                if(gameBoard.getPiece(temp).getTeamColor()==teamColor)
                    if(!validMoves(temp).isEmpty())
                        stalemate = false;
            }
        }
        return stalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        //throw new RuntimeException("Not implemented");
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
