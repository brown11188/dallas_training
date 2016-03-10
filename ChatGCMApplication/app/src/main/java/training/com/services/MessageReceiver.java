package training.com.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import retrofit2.Retrofit;
import training.com.common.RetrofitCallBackUtil;
import training.com.common.RetrofitGenerator;
import training.com.dao.RESTDatabaseDAO;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RetrofitGenerator retrofitGenerator = new RetrofitGenerator();
        RetrofitCallBackUtil retrofitCallBackUtil = new RetrofitCallBackUtil();
        Bundle bundle = intent.getExtras();
        Intent broadcastIntent = new Intent("Msg");
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String message = bundle.getString("message");
        String name = bundle.getString("title");
        broadcastIntent.putExtra("message", message);
        broadcastIntent.putExtra("name", name);
        Retrofit client = retrofitGenerator.createRetrofit();
        RESTDatabaseDAO service = client.create(RESTDatabaseDAO.class);
        retrofitCallBackUtil.addMessageToServerRetrofit(message, retrofitCallBackUtil.getUserByNameRetrofit(name, service), service);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
        Intent gcmIntent = new Intent(context, MessageService.class);
        gcmIntent.putExtras(intent.getExtras());
        startWakefulService(context, gcmIntent);
        setResultCode(Activity.RESULT_OK);
    }
}
