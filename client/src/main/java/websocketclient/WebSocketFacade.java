package websocketclient;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;

import ui.DrawBoard;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//based on petshop
public class WebSocketFacade extends Endpoint {

    Session session;
    ChessGame currentGame;
    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage genericNotification = new Gson().fromJson(message, ServerMessage.class);
                   // notificationHandler.notify(notification);
                    ServerMessage.ServerMessageType type = genericNotification.getServerMessageType();
                    switch(type){
                        case ERROR:
                            ServerError error = new Gson().fromJson(message, ServerError.class);
                            System.out.println(error.getErrorMessage());
                            System.out.println("\n>>>");
                        case NOTIFICATION:
                            ServerNotification notification = new Gson().fromJson(message, ServerNotification.class);
                            System.out.println(notification.getNotifMessage());
                            System.out.println("\n>>>");
                        case LOAD_GAME:
                            ServerLoadGame loadGame = new Gson().fromJson(message, ServerLoadGame.class);
                            ChessGame game = loadGame.getGame();
                            currentGame = game;
                            ServerLoadGame.PlayerType playerType = loadGame.getPlayerType();
                            DrawBoard drawBoard;
                            if(playerType== ServerLoadGame.PlayerType.BLACK){
                                drawBoard = new DrawBoard(false, game);
                            }else{
                                drawBoard = new DrawBoard(true, game);
                            }
                            drawBoard.print();
                            System.out.println("\n>>>");
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
    //based on petshop
    public void sendUserCommand (UserGameCommand command) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void updateGame(ChessGame game){
        currentGame = game;
    }

    public ChessGame getCurrentGame(){return currentGame;}

}