package training.com.dao;

import java.util.Date;

import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 2/1/16.
 */
public interface DatabaseDAO {

    void addUser(Users user);
    void addMessage(String message, String expires_date, int user_id );
}
