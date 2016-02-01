package training.com.model;

import java.util.Date;

/**
 * Created by enclaveit on 2/1/16.
 */
public class ChatContent {

    private int userId;
    private String message;
    private Date expiresTime;

    public ChatContent() {
    }

    public ChatContent(int userId, String message, Date expiresTime) {
        this.userId = userId;
        this.message = message;
        this.expiresTime = expiresTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }
}
