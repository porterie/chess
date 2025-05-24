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

}
