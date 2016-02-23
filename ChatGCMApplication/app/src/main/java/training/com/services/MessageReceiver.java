package training.com.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import training.com.common.AppConfig;
import training.com.common.TimeUtil;
import training.com.database.DatabaseHelper;
import training.com.model.Users;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        TimeUtil timeUtil = new TimeUtil();
        Intent broadcastIntent = new Intent("Msg");
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String message = bundle.getString("message");
        String name = bundle.getString("title");
        broadcastIntent.putExtra("message", message);
        broadcastIntent.putExtra("name", name);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        Users user = databaseHelper.getUser(name);
        databaseHelper.addMessage(message, timeUtil.getCurrentTime(), AppConfig.USER_ID, user.getUserId());
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
        Intent gcmIntent = new Intent(context, MessageService.class);
        gcmIntent.putExtras(intent.getExtras());
        startWakefulService(context, gcmIntent);
        setResultCode(Activity.RESULT_OK);
    }
}
