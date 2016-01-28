package training.com.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Intent br_intent = new Intent("Msg");
        br_intent.putExtra("message", bundle.getString("message"));
        br_intent.putExtra("from", bundle.getString("from"));

        LocalBroadcastManager.getInstance(context).sendBroadcast(br_intent);
        Intent gcmIntent =  new Intent(context, MessageService.class);
        gcmIntent.putExtras (intent.getExtras());
        startWakefulService(context, gcmIntent);
        setResultCode(Activity.RESULT_OK);
    }
}
