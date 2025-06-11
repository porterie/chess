package websocket.commands;

public class UserConnect extends UserGameCommand {

    String color;
    CommandType commandType;
    public UserConnect(CommandType commandType, String authToken, Integer gameID, String color) {
        super(commandType, authToken, gameID);

        this.color = color;
        commandType = CommandType.CONNECT;
    }
    public String getColor () {return color;}

}
