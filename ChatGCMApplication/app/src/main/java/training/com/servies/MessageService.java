package training.com.servies;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageService extends IntentService {

    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        Log.i("Message Service", bundle.getString("message"));
        MessageReceiver.completeWakefulIntent(intent);
    }
}
