package training.com.dao;

import java.util.ArrayList;

import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 3/11/16.
 */
public interface RetrofitResponseCallBack {
    void onSuccessMessages(ArrayList<Message> messages);
    void onSuccessUser(Users user);
    void onFailure();
}
