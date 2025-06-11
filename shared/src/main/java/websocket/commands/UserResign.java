package websocket.commands;

public class UserResign extends UserGameCommand {

    String color;
    CommandType commandType;
    public UserResign(CommandType commandType, String authToken, Integer gameID, String color) {
        super(commandType, authToken, gameID);
        this.color = color;

        commandType = CommandType.RESIGN;
    }

    public String getColor() {return color;}
}
