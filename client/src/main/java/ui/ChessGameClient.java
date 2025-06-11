package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.ServerFacade;
import websocket.commands.UserGameCommand;
import websocket.commands.UserLeave;
import websocket.commands.UserMove;
import websocket.commands.UserResign;
import websocket.messages.ServerLoadGame;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ChessGameClient  {
    private final String serverUrl;
    private final ServerFacade server;
    Gson gson = new Gson();
    Integer gameID;
    ServerLoadGame.PlayerType playerType;
    String username;
    private final Set<String> validColumns = Set.of("1", "2", "3", "4", "5", "6", "7", "8");
    private final Set<String> validRows = Set.of("a", "b", "c", "d", "e", "f", "g", "h");
    private static final Map<String, Integer> letterToNumber = Map.of(
            "a", 1, "b", 2, "c", 3, "d", 4,
            "e", 5, "f", 6, "g", 7, "h", 8
    );
    private static final Set<String> promotionPieces = Set.of("QUEEN", "ROOK", "BISHOP", "KNIGHT");
    private static final Map<String, ChessPiece.PieceType> pieceMap = Map.of(
            "QUEEN", ChessPiece.PieceType.QUEEN, "ROOK", ChessPiece.PieceType.ROOK,
            "BISHOP", ChessPiece.PieceType.BISHOP, "KNIGHT", ChessPiece.PieceType.KNIGHT
    );
    public ChessGameClient(String serverUrl, ServerFacade server, Integer gameID, ServerLoadGame.PlayerType playerType, String username){
        this.serverUrl = serverUrl;
        this.server = server;
        this.playerType = playerType;
        this.gameID = gameID;
        this.username = username;
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highLightMoves(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String highLightMoves(String... params){
        //todo: implement
        return "FIXME: highlight moves for selected piece";
    }

    public String redraw(){
        ChessGame game = server.getCurrentGame();
        DrawBoard drawBoard;
        if(playerType== ServerLoadGame.PlayerType.BLACK){
            drawBoard = new DrawBoard(false, game);
            drawBoard.print();
        }else{
            drawBoard = new DrawBoard(true, game);
            drawBoard.print();
        }
        return "\n";
    }
    private boolean validInput(String startRow, String startCol, String endRow, String endCol){
        if(validRows.contains(startRow) && validColumns.contains(startCol) && validRows.contains(endRow) && validColumns.contains(endCol)){
            return true;
        }else{
            return false;
        }
    }
    public String makeMove(String... params) throws ResponseException {
        if(params.length==4){//no promotion
            if(validInput(params[0], params[1], params[2], params[3])){
                int startCol, endCol;
                try {
                    startCol = Integer.parseInt(params[1]);
                    endCol = Integer.parseInt(params[3]);
                }catch(NumberFormatException e){
                    return "Invalid column";
                }
                int startRow = letterToNumber.get(params[0]);
                int endRow = letterToNumber.get(params[2]);
                ChessPosition start = new ChessPosition(startRow, startCol);
                ChessPosition end = new ChessPosition(endRow, endCol);
                ChessMove move = new ChessMove(start, end, null);
                UserMove moveCommand = new UserMove(UserGameCommand.CommandType.MAKE_MOVE, server.getAuthToken(), gameID, move);
                try{server.playerMove(moveCommand);}
                catch(ResponseException e){
                    System.out.println(e.getMessage());
                    return "\n>>>";
                }
                return "Submitted move\n";
            }else{
                return "Expected <start row> <start col> <end row> <end col> OPTIONAL:<promotion piece>\n" +
                        "ie: QUEEN || ROOK || BISHOP || KNIGHT";
            }

        }else if(params.length==5){
            if(validInput(params[0], params[1], params[2], params[3]) && promotionPieces.contains(params[4])){
                int startCol, endCol;
                try {
                    startCol = Integer.parseInt(params[1]);
                    endCol = Integer.parseInt(params[3]);
                }catch(NumberFormatException e){
                    return "Invalid column";
                }
                int startRow = letterToNumber.get(params[0]);
                int endRow = letterToNumber.get(params[2]);
                ChessPosition start = new ChessPosition(startRow, startCol);
                ChessPosition end = new ChessPosition(endRow, endCol);
                ChessPiece.PieceType promotionPiece = pieceMap.get(params[4]);
                ChessMove move = new ChessMove(start, end, promotionPiece);
                UserMove moveCommand = new UserMove(UserGameCommand.CommandType.MAKE_MOVE, server.getAuthToken(), gameID, move);
                server.playerMove(moveCommand);
                return "Submitted move\n";
            }else{
                return "Expected <start row> <start col> <end row> <end col> OPTIONAL:<promotion piece>\n" +
                        "ie: QUEEN || ROOK || BISHOP || KNIGHT";
            }

        }else{
            return "Expected <start row> <start col> <end row> <end col> OPTIONAL:<promotion piece>\n" +
                    "ie: QUEEN || ROOK || BISHOP || KNIGHT";
        }
    }

    public String leave(){
        String color;
        switch(playerType){
            case BLACK:
                color = "BLACK";
            case WHITE:
                color = "WHITE";
            default:
                color = "OBSERVER";
        }
        UserLeave leaveCommand = new UserLeave(UserGameCommand.CommandType.LEAVE, server.getAuthToken(), gameID, color);
        try{server.sendCommand(leaveCommand);
            return "Left game\n";
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            return "Error while leaving\n";
        }

    }

    public String resign(){
        String color;
        switch(playerType){
            case BLACK:
                color = "BLACK";
            case WHITE:
                color = "WHITE";
            default:
                color = "OBSERVER";
        }
        UserResign resignCommand = new UserResign(UserGameCommand.CommandType.RESIGN, server.getAuthToken(), gameID, color);
        try{server.sendCommand(resignCommand);
            return "Resigned game\n";
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            return "Error while resigning\n";
        }
    }
    public String help() {
        return """
                - chessgame help menu (fix me)
                """;
    }
}
