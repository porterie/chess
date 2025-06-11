package websocket.commands;

public class UserConnect extends UserGameCommand {
    String username;
    String color;
    CommandType type;
    public UserConnect(CommandType commandType, String authToken, Integer gameID, String username, String color) {
        super(commandType, authToken, gameID);
        this.username = username;
        this.color = color;
        type = CommandType.CONNECT;
    }
    public String getColor () {return color;}

    public String getUsername () {return username;};
}
