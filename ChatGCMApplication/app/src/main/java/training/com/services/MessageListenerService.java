package training.com.services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageListenerService extends GcmListenerService {

    private static final String TAG = "Message Service";

    @Override
    public void onMessageReceived(String from, Bundle data) {

    }
}
