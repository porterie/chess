package websocket.commands;

public class UserResign extends UserGameCommand {
    String username;
    String color;
    CommandType commandType;
    public UserResign(CommandType commandType, String authToken, Integer gameID, String username, String color) {
        super(commandType, authToken, gameID);
        this.color = color;
        this.username = username;
        commandType = CommandType.RESIGN;
    }
    public String getUsername() {return username;}
    public String getColor() {return color;}
}
