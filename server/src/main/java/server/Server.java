package server;

import handler.Handler;
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
        Spark.post("/user", (request, response) -> Handler.registerHandler(request, response, userService));
        Spark.post("/session", (request, response) -> Handler.loginHandler(request, response, userService));
        Spark.delete("/session", (request, response) -> Handler.logoutHandler(request, response, userService));
        Spark.get("/game", (request, response) -> Handler.listGamesHandler(request, response, gameService, userService));
        Spark.post("/game", (request, response) -> Handler.createGameHandler(request, response, gameService, userService));
        Spark.put("/game", (request, response) -> Handler.joinGameHandler(request, response, gameService, userService));
        Spark.delete("/db", (request, response) -> Handler.clearHandler(request, response, gameService, userService));
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
