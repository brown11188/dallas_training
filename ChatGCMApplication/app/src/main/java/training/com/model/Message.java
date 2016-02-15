package training.com.model;

import android.util.Log;

import java.util.Date;

/**
 * Created by enclaveit on 2/1/16.
 */
public class Message {
    private static Message mInstance = null;

    private int message_id;
    private int userId;
    private String message;
    private Date expiresTime;
    private int sender_id;

    public Message() {
    }

    public static Message getInstance() {
        if (mInstance == null) {
            mInstance = new Message();
        }
        return mInstance;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
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

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }
}
