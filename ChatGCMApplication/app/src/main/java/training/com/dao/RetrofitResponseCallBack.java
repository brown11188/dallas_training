package training.com.dao;

import java.util.ArrayList;

import training.com.model.Message;

/**
 * Created by enclaveit on 3/11/16.
 */
public interface RetrofitResponseCallBack {
    void onSuccess(ArrayList<Message> messages);
    void onFailure();
}
