package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyUnitTests {

    public MyUnitTests() {
    }

    UserService userService = new UserService();
    GameService gameService = new GameService();

    @Test
    @Order(1)
    @DisplayName("Register User")
    public void registerTestPositive() {
        RegisterResult result = userService.register(new RegisterRequest("user name", "password", "email@email.com"));
        Assertions.assertEquals("user name", result.username(), "Registers new user");
    }

    @Test
    @Order(2)
    public void registerTestNegative(){
        RegisterResult result = userService.register(new RegisterRequest(null, "pass", null));
        Assertions.assertNull(result, "Tests service will not allow null fields for registry");
    }

    @Test
    @Order(3)
    public void loginTestPos(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult result = userService.login(new LoginRequest("username", "password"));
        Assertions.assertNotNull(result.authToken(), "logs in user with token");
    }

    @Test
    @Order(4)
    public void loginTestNeg(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult result = userService.login(new LoginRequest("username", "wrongpassword"));
        Assertions.assertNull(result.authToken(), "rejects login attempt");
    }

    @Test
    @Order(5)
    public void logoutTestPos(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        LogoutResult result = userService.logout(new LogoutRequest(loginResult.authToken()));
        Assertions.assertTrue(result.success(), "logs out user");
    }

    @Test
    @Order(6)
    public void logoutTestNeg(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        LogoutResult result = userService.logout(new LogoutRequest("bad token"));
        Assertions.assertFalse(result.success(), "doesnt log out user");
    }

    @Test
    @Order(7)
    public void createGameTestPos(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        CreateGameResult result = null;
        try {
            result = gameService.createGame("myGame");
        } catch (DataAccessException e) {
            System.out.println("whoopsie daisy");
        }
        Assertions.assertNotNull(result);
    }

    @Test
    @Order(8)
    public void createGameTestNeg(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        CreateGameResult result = null;
        try {
            result = gameService.createGame(null);
        } catch (DataAccessException e) {
            System.out.println("whoopsie daisy");
        }
        Assertions.assertNull(result);
    }

    @Test
    @Order(9)
    public void joinGameTestPos(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        CreateGameResult createResult = null;
        boolean playerJoined = false;
        try {
            createResult = gameService.createGame("myGame");
            gameService.joinGame("WHITE", createResult.gameID(), "username");
            playerJoined = !gameService.whiteFree(createResult.gameID());
        } catch (DataAccessException e) {
            System.out.println("whoopsie daisy");
        }
        Assertions.assertTrue(playerJoined);
    }
    @Test
    @Order(10)
    public void joinGameTestNeg(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        CreateGameResult createResult = null;
        boolean playerJoined = false;
        try {
            createResult = gameService.createGame("myGame");
            gameService.joinGame("WH00TE", createResult.gameID(), "username");
            playerJoined = !gameService.whiteFree(createResult.gameID());
        } catch (DataAccessException e) {
            System.out.println("whoopsie daisy");
        }
        Assertions.assertFalse(playerJoined);
    }

    @Test
    @Order(11)
    public void listGameTestPos(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        CreateGameResult createResult = null;
        String gameList = null;
        try {
            createResult = gameService.createGame("myGame");
            gameService.joinGame("WH00TE", createResult.gameID(), "username");
            gameList = gameService.listGames();
        } catch (DataAccessException e) {
            System.out.println("whoopsie daisy");
        }
        Assertions.assertNotNull(gameList);
    }

    @Test
    @Order(12)
    public void listGameTestNeg(){
        RegisterResult registerResult = userService.register(new RegisterRequest("username", "password", "email@email.com"));
        LoginResult loginResult = userService.login(new LoginRequest("username", "password"));
        CreateGameResult createResult = null;
        String gameList = null;
        boolean stringShort = false;
        try {
            createResult = gameService.createGame("myGame");
            gameService.joinGame("WH00TE", createResult.gameID(), "username");
            gameList = gameService.listGames();
        } catch (DataAccessException e) {
            System.out.println("whoopsie daisy");
        }
        Assertions.assertNotNull(gameList);
        stringShort = gameList.length()<100;
        Assertions.assertTrue(stringShort);//no hallucinated games
    }

}
