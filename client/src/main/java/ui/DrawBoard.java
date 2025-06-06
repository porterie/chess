package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.lang.reflect.Array;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private ChessGame game;
    StringBuilder displayBoard;
    Boolean perspectiveWhite;
    String background = SET_BG_COLOR_BLUE;
    String light = SET_BG_COLOR_LIGHT_GREY;
    String dark = SET_BG_COLOR_DARK_GREEN;
    public DrawBoard(Boolean perspectiveWhite, ChessGame game){
        this.game = game;
        this.perspectiveWhite = perspectiveWhite;
        displayBoard = new StringBuilder();
    }

    public void print(){
        if(perspectiveWhite) {
            displayBoard.append(SET_BG_COLOR_BLUE);
            //chess square 3 chars long. BG color changes between the spaces between the chars
            displayBoard.append(background + "    a  b  c  d  e  f  g  h    \n"); //top row always the same
            for(int i = 8; i>=1; i--){//count down rows
                displayBoard.append(" " + i + " ");
                for(int j = 1; j<= 8; j++){//count accross columns
                    String color;
                    if(i%2 == j%2){
                        color = dark;
                    }else{
                        color = light;
                    }
                    displayBoard.append(color + " " + getPiece(i,j) + " ");
                }
                displayBoard.append(background + " " + i + " \n");
            }
            displayBoard.append(background + "    a  b  c  d  e  f  g  h    \n");
        }else{//perspective black
            displayBoard.append(SET_BG_COLOR_BLUE);
            //chess square 3 chars long. BG color changes between the spaces between the chars
            displayBoard.append(background + "    h  g  f  e  d  c  b  a    \n"); //top row always the same
            for(int i = 1; i<=8; i++){//count up rows
                displayBoard.append(" " + i + " ");
                for(int j = 8; j>= 1; j--){//count back  accross columns
                    String color;
                    if(i%2 == j%2){
                        color = dark;
                    }else{
                        color = light;
                    }
                    displayBoard.append(color + " " + getPiece(i,j) + " ");
                }
                displayBoard.append(background + " " + i + " \n");
            }
            displayBoard.append(background + "    h  g  f  e  d  c  b  a    \n");
        }
        System.out.print(displayBoard.toString());
    }

    private String getPiece(Integer row, Integer col){
        ChessPiece.PieceType piece;
        if(game.getBoard().getPiece(new ChessPosition(row,col)) != null) {
            piece = game.getBoard().getPiece(new ChessPosition(row, col)).getPieceType();
        }else{
            piece = null;
        }
        boolean isWhite = (game.getBoard().getPiece(new ChessPosition(row, col)).getTeamColor()== ChessGame.TeamColor.WHITE);
        switch(piece){
            case KING:
                if(isWhite){return WHITE_KING;}else{return BLACK_KING;}
            case QUEEN:
                if(isWhite){return WHITE_QUEEN;}else{return BLACK_QUEEN;}
            case ROOK:
                if(isWhite){return WHITE_ROOK;}else{return BLACK_ROOK;}
            case BISHOP:
                if(isWhite){return WHITE_BISHOP;}else{return BLACK_BISHOP;}
            case KNIGHT:
                if(isWhite){return WHITE_KNIGHT;}else{return BLACK_KNIGHT;}
            case PAWN:
                if(isWhite){return WHITE_PAWN;}else{return BLACK_PAWN;}
            case null:
                return " ";
        }
    }

}
