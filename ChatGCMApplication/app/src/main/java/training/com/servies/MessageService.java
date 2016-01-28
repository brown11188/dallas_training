package training.com.servies;

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

import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;

/**
 * Created by enclaveit on 1/27/16.
 */
public class MessageService extends IntentService {

    private SharedPreferences preferences;
    private BroadcastReceiver myReceiver;

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
            Log.i("Success", "Receive message successful");
            sendDataToActivity(bundle.getString("from"), bundle.getString("message"));
            sendNotification(bundle.getString("message"));
        }
        MessageReceiver.completeWakefulIntent(intent);
    }

    private void sendDataToActivity(String from, String message) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("from", from);
            bundle.putString("message", message);
            Intent chat = new Intent(this, ChatActivity.class);
            chat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            chat.putExtra("INFO", bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, chat, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.send(this, 1000, chat);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String message) {

        Log.i("Notification", "In notification method");
        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("New message")
                .setSound(defaultSoundUri)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());


    }
}
