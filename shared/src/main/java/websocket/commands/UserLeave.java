package websocket.commands;

public class UserLeave extends UserGameCommand{
    String username;
    String color;
    CommandType commandType;
    public UserLeave(CommandType commandType, String authToken, Integer gameID, String username, String color) {
        super(commandType, authToken, gameID);
        this.username = username;
        this.color = color;
        commandType = CommandType.LEAVE;
    }

    public String getColor() {return color;}

    public String getUsername(){return username;}
}
