package training.com.dao;

import java.util.ArrayList;
import java.util.List;

import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 2/1/16.
 */
public interface DatabaseDAO {

    void addUser(Users user);
    void addMessage(String message, String expires_date, int user_id );
    ArrayList<Users> getUsers();
    Users getUser(String registration_id);
    List<Message> getMessges(int user_id);
    Message getLastMessage(int user_id);
}
