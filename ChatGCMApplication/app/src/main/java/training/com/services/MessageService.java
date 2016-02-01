package training.com.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.List;

import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageService extends IntentService {

    private List<String> message_array = new ArrayList<>();


    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);



        String messageType = gcm.getMessageType(intent);
        if (!bundle.isEmpty()) {
//            GCM Server return message_type = null
//            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
//                Log.e("Error", "Send message error");
//            } else if( GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                Log.e("Delete", "Some messages have been discarded");
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                sendDataToActivity(bundle.getString("from"),bundle.getString("message") );
//                Log.i("Success", "Receive message successful");
//            }
            sendNotification(bundle.getString("title"), bundle.getString("message"));

        }
        MessageReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(String from, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("name", from);
        bundle.putString("message", message);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("INFO", bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("New message")
                .setSound(defaultSoundUri)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

}
