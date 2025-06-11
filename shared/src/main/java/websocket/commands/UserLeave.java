package websocket.commands;

public class UserLeave extends UserGameCommand{
    String color;
    CommandType commandType;
    public UserLeave(CommandType commandType, String authToken, Integer gameID, String color) {
        super(commandType, authToken, gameID);
        this.color = color;
        commandType = CommandType.LEAVE;
    }

    public String getColor() {return color;}

}
