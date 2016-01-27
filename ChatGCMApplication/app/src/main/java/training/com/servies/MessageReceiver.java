package training.com.servies;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Intent msg_intent = new Intent("Msg");
        msg_intent.putExtra("msg", bundle.getString("message"));
        Log.i("Receiver",  bundle.getString("message"));
        LocalBroadcastManager.getInstance(context).sendBroadcast(msg_intent);

        startWakefulService(context, msg_intent);
        setResultCode(Activity.RESULT_OK);
    }
}
