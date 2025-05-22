package server;

import handler.handler;
import service.UserService;
import spark.*;

public class Server {
    UserService userService = new UserService();
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (request, response) -> handler.registerHandler(request, response, userService));
        Spark.post("/session", (request, response) -> handler.loginHandler(request, response, userService));
        Spark.delete("/session", (request, response) -> handler.logoutHandler(request, response, userService));
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
