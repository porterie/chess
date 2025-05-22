package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
public class MemoryUserDAO implements UserDAO {
    //based on petshop MemoryDataAccess

    final private ArrayList<UserData> users = new ArrayList<>();
    public void clear() throws DataAccessException {
        users.clear();
    }

    public void createUser(UserData user) throws DataAccessException {
        //user = new UserData(user.getUsername(), user.getEmail(), user.getPasswd());
        users.add(user);
    }

    public UserData getUser(String username) throws DataAccessException {
        UserData foundUser = null;
        for (UserData user : users){
            if(user.getUsername().equals(username)){
                foundUser = user;
                break;
            }
        }
        return foundUser;
    }

    public void deleteUser(String username) throws DataAccessException {
        UserData delTarget = null;
        for (UserData user : users){
            if(user.getUsername().equals(username)){
                delTarget = user;
            }
        }
        if(delTarget!=null){
            users.remove(delTarget);
        }
    }
}
