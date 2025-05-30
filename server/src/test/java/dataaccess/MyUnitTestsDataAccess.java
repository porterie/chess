package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import service.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyUnitTestsDataAccess {

    public MyUnitTestsDataAccess() {
    }

    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    @BeforeEach
    public void setup(){
        try {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    {
        try {
            userDAO = new MySqlUserDAO();
            gameDAO = new MySqlGameDAO();
            authDAO = new MySqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    UserData testUser = new UserData("goodUser", "email.com", "password");
    UserData testUser2 = new UserData("gooderUser", "hotmail.tv", "psswrd");
    @Test
    @Order(1)

    public void createUserPositive() throws DataAccessException {
        userDAO.createUser(testUser);
        UserData user = userDAO.getUser("goodUser");
        Assertions.assertNotNull(user, "adds and retrieves user from database");
    }

    @Test
    @Order(2)
    public void createUserNegative() throws DataAccessException {
        userDAO.createUser(testUser);
        boolean replicaCausesException = false;
        try{
            userDAO.createUser(testUser);
        }catch(DataAccessException exception){
            replicaCausesException = true;
        }
        Assertions.assertTrue(replicaCausesException, "does not allow duplicate user");
    }

    @Test
    @Order(3)
    public void getUserPositive() throws DataAccessException {
        userDAO.createUser(testUser);
        userDAO.createUser(testUser2);
        String email2 = userDAO.getUser("gooderUser").getEmail();

        Assertions.assertEquals("hotmail.tv", email2, "Gets right user");
    }

    @Test
    @Order(4)
    public void getUserNegative() throws DataAccessException {
        userDAO.createUser(testUser);
        userDAO.createUser(testUser2);
        UserData fakeUser = userDAO.getUser("goodestUser");

        Assertions.assertNull(fakeUser, "doesn't return fake user");
    }

}
