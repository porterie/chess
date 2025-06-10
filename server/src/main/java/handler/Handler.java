package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class Handler {

    public static Object registerHandler(Request request, Response response, UserService userService) {
        Gson serializer = new Gson();
        RegisterRequest regReq = serializer.fromJson(request.body(), RegisterRequest.class);

        try{
        RegisterResult result = userService.register(regReq);
            response.type("application/json");
            if (result == null) {
                response.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            } else if (result.username() == null) {
                response.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            } else if (result.username().equals(regReq.username()) && result.authToken() != null) {
                response.status(200);
                return serializer.toJson(result);
            } else {
                response.status(500);
                return "{ \"message\": \"Error: Database error.\" }";
            }
        }catch(DataAccessException exception){
            response.status(500);
            return "{ \"message\": \"Error: Database error.\" }";
        }
    }
    public static Object loginHandler(Request request, Response response, UserService userService) {
        Gson serializer = new Gson();
        LoginRequest logReq = serializer.fromJson(request.body(), LoginRequest.class);
        try {
            LoginResult result = userService.login(logReq);
            response.type("application/json");
            if (logReq.username() == null || logReq.password() == null || result == null) {
                response.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            } else if (result.username() == null) {
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else if (result.authToken() == null) {
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else if (Objects.equals(result.username(), logReq.username())) {
                response.status(200);
                return serializer.toJson(result);
            }else{
                response.status(500);
                return "{\"message\": \"Error: idk\"}";
            }
        } catch (DataAccessException exception) {
            response.type("application/json");
            response.status(500);
            return "{ \"message\": \"Error: data access.\" }";
        }
    }

    public static Object logoutHandler(Request request, Response response, UserService userService) {
        try {
            Gson serializer = new Gson();
            LogoutRequest logOReq = new LogoutRequest(request.headers("Authorization"));

            LogoutResult result = userService.logout(logOReq);
            response.type("application/json");
            if (!result.userFound()) {
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else {
                response.status(200);
                return "{}";
            }
        }catch(DataAccessException exception){
            response.type("application/json");
            response.status(500);
            return "{ \"message\": \"Error: data access failure.\" }";
        }

    }

    public static Object listGamesHandler(Request request, Response response, GameService gameService, UserService userService) throws DataAccessException {
        Gson serializer = new Gson();
        String authToken = request.headers("Authorization");
        response.type("application/json");
        try {
            if (userService.authenticationValid(authToken)) {
                response.status(200);
                return gameService.listGames();
            } else {
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            }
        }catch(DataAccessException exception){
            response.status(500);
            return "{ \"message\": \"Error: database has a boo boo\" }";

        }
    }
    public static Object createGameHandler(Request request, Response response, GameService gameService, UserService userService) throws DataAccessException {
        try {
            Gson serializer = new Gson();
            String authToken = request.headers("Authorization");
            CreateGameRequest createGameRequest = serializer.fromJson(request.body(), CreateGameRequest.class);
            response.type("application/json");
            if (createGameRequest.gameName() == null) {
                response.status(400);
                return "{ \"message\": \"Error: bad request\" }";

            } else if (userService.authenticationValid(authToken)) {
                CreateGameResult result = gameService.createGame(createGameRequest.gameName());
                response.status(200);
                return serializer.toJson(result);
            } else {
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            }
        }catch(DataAccessException exception){
            response.status(500);
            return "{ \"message\": \"Error: Data access screwup\" }";

        }
    }

    public static Object joinGameHandler(Request request, Response response, GameService gameService, UserService userService) {
        Gson serializer = new Gson();
        String authToken = request.headers("Authorization");
        response.type("application/json");
        try {
            JoinGameRequest joinGameRequest = serializer.fromJson(request.body(), JoinGameRequest.class);

            boolean validAuth = userService.authenticationValid(authToken);
            boolean colorWhite = (Objects.equals(joinGameRequest.playerColor(), "WHITE"));
            boolean colorBlack = (Objects.equals(joinGameRequest.playerColor(), "BLACK"));
            boolean invalidColor = !(colorWhite || colorBlack);
            Integer gameID = joinGameRequest.gameID();
            boolean validGameID = gameService.validGame(gameID);
            if(!validAuth){
                response.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            }else if(!validGameID || invalidColor){
                response.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            }else if ((colorWhite && !gameService.whiteFree(gameID)) ||
                    (colorBlack && !gameService.blackFree(gameID))){
                response.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            }else{
                String playerName = userService.getUser(authToken);
                gameService.joinGame(joinGameRequest.playerColor(), joinGameRequest.gameID(), playerName);
                response.status(200);
                return"{}";
            }

        }catch(DataAccessException exception){
            response.status(500);
            return "{ \"message\": \"Error: Database problem\" }";
        }
    }

    public static Object clearHandler(Request request, Response response, GameService gameService, UserService userService) {
        try{
            userService.clearMemory();
            gameService.clearMemory();
            response.status(200);
            return "{}";
        }catch(DataAccessException exception){
            response.status(500);
            return "{ \"message\": \"Error: Database problem\" }";
        }
    }
}
