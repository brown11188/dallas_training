package training.com.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.ArrayList;

import retrofit2.Retrofit;
import training.com.common.AppConfig;
import training.com.common.RetrofitCallBackUtil;
import training.com.common.RetrofitGenerator;
import training.com.dao.RESTDatabaseDAO;
import training.com.dao.RetrofitResponseCallBack;
import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RetrofitGenerator retrofitGenerator = new RetrofitGenerator();
        final RetrofitCallBackUtil retrofitCallBackUtil = new RetrofitCallBackUtil();
        Bundle bundle = intent.getExtras();
        Intent broadcastIntent = new Intent("Msg");
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final String message = bundle.getString("message");
        String name = bundle.getString("title");
        broadcastIntent.putExtra("message", message);
        broadcastIntent.putExtra("name", name);
        Retrofit client = retrofitGenerator.createRetrofit();
        final RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);
        retrofitCallBackUtil.getUserByNameRetrofit(name, service, new RetrofitResponseCallBack() {
            @Override
            public void onSuccessMessages(ArrayList<Message> messages) {
            }

            @Override
            public void onSuccessUser(Users user) {
                retrofitCallBackUtil.addMessageToServer(message, AppConfig.USER_ID, user.getUserId(), service);
            }

            @Override
            public void onFailure() {

            }
        });

        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
        Intent gcmIntent = new Intent(context, MessageService.class);
        gcmIntent.putExtras(intent.getExtras());
        startWakefulService(context, gcmIntent);
        setResultCode(Activity.RESULT_OK);
    }
}
