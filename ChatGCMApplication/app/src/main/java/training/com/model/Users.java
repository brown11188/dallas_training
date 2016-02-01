package training.com.model;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by enclaveit on 2/1/16.
 */
public class Users {
    private int userId;
    private String userName;
    private String password;
    private String registrationId;

    private Collection<Message> messages = new HashSet<>(0);

    public Users() {
    }

    public Users(Collection<Message> messages, String registrationId, String password, String userName, int userId) {
        this.messages = messages;
        this.registrationId = registrationId;
        this.password = password;
        this.userName = userName;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public Collection<Message> getChatContent() {
        return messages;
    }

    public void setChatContent(Collection<Message> chatContent) {
        this.messages = messages;
    }
}
