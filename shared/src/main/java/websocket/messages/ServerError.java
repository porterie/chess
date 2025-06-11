package websocket.messages;

public class ServerError extends ServerMessage {
    String errorMessage;
    ServerMessageType serverMessageType;
    public ServerError(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
        serverMessageType = ServerMessageType.ERROR;
    }
    public String getErrorMessage() {return errorMessage;}
}
