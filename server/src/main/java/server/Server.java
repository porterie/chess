package server;

import handler.handler;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    UserService userService = new UserService();
    GameService gameService = new GameService();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (request, response) -> handler.registerHandler(request, response, userService));
        Spark.post("/session", (request, response) -> handler.loginHandler(request, response, userService));
        Spark.delete("/session", (request, response) -> handler.logoutHandler(request, response, userService));
        Spark.get("/game", (request, response) -> handler.listGamesHandler(request, response, gameService, userService));
        Spark.post("/game", (request, response) -> handler.createGameHandler(request, response, gameService, userService));
        Spark.put("/game", (request, response) -> handler.joinGameHandler(request, response, gameService, userService));
        Spark.delete("/db", (request, response) -> handler.clearHandler(request, response, gameService, userService));
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
