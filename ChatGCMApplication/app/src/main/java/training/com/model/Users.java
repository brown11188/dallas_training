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

    private Collection<ChatContent> chatContent = new HashSet<>(0);

    public Users() {
    }

    public Users(Collection<ChatContent> chatContent, String registrationId, String password, String userName, int userId) {
        this.chatContent = chatContent;
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

    public Collection<ChatContent> getChatContent() {
        return chatContent;
    }

    public void setChatContent(Collection<ChatContent> chatContent) {
        this.chatContent = chatContent;
    }
}
