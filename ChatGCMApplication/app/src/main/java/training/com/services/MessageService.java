package training.com.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import training.com.chatgcmapplication.ChatActivity;
import training.com.chatgcmapplication.R;
import training.com.common.AppConfig;

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
        if (!bundle.isEmpty()&& AppConfig.REG_ID != null) {
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
                .setContentTitle(from)
                .setSound(defaultSoundUri)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

}
