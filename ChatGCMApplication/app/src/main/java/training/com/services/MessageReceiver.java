package training.com.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import training.com.model.PreferenceObject;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageReceiver extends WakefulBroadcastReceiver {

    private static final String PREF_NAME = "CHAT";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        Intent br_intent = new Intent("Msg");
        br_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        br_intent.putExtra("message", bundle.getString("message"));
        br_intent.putExtra("from", bundle.getString("from"));
        Log.i("HEHEHE", bundle.getString("message"));
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Collection<String> set =  preferences.getStringSet("message_set", new HashSet<String>());

        set.add(bundle.getString("message"));
        editor.putStringSet("message_set", (Set<String>) set);
        editor.commit();

        LocalBroadcastManager.getInstance(context).sendBroadcast(br_intent);
        Intent gcmIntent = new Intent(context, MessageService.class);
        gcmIntent.putExtras(intent.getExtras());
        startWakefulService(context, gcmIntent);
        setResultCode(Activity.RESULT_OK);
    }


}
