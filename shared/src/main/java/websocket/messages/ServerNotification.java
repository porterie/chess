package websocket.messages;

public class ServerNotification extends ServerMessage {
    String message;
    ServerMessageType serverMessageType;
    public ServerNotification(ServerMessageType type, String message) {
        super(type);
        this.message = message;
        serverMessageType = ServerMessageType.NOTIFICATION;
    }
}
