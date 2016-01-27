package training.com.contact;

/**
 * Created by hawk on 27/01/2016.
 */
public class Contact {
    private String userId;
    private String userName;
    private String lastMsg;

    public Contact(String userId, String userName, String lastMgs) {
        this.userId = userId;
        this.userName = userName;
        this.lastMsg = lastMgs;
    }

    public Contact() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMgs) {
        this.lastMsg = lastMgs;
    }
}
