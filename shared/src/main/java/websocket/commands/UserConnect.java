package websocket.commands;

public class UserConnect extends UserGameCommand {
    String username;
    String color;
    CommandType commandType;
    public UserConnect(CommandType commandType, String authToken, Integer gameID, String username, String color) {
        super(commandType, authToken, gameID);
        this.username = username;
        this.color = color;
        commandType = CommandType.CONNECT;
    }
    public String getColor () {return color;}

    public String getUsername () {return username;};
}
