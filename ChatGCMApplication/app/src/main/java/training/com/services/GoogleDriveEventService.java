package training.com.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.drive.events.DriveEventService;

import training.com.common.AppConfig;

/**
 * Created by hawk on 01/04/2016.
 */
public class GoogleDriveEventService extends DriveEventService {
    private static final String TAG = "_X_";

    @Override
    public void onCompletion(CompletionEvent event) {
        super.onCompletion(event);
        DriveId driveId = event.getDriveId();
        String fileId = driveId.getResourceId();
        Intent intent = new Intent("GoogleDrive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("driveId", fileId);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        Log.d(TAG, "onComplete: " + driveId);
        Log.i("WTF", fileId);
        switch (event.getStatus()) {
            case CompletionEvent.STATUS_CONFLICT:
                Log.d(TAG, "STATUS_CONFLICT");
                event.dismiss();
                break;
            case CompletionEvent.STATUS_FAILURE:
                Log.d(TAG, "STATUS_FAILURE");
                event.dismiss();
                break;
            case CompletionEvent.STATUS_SUCCESS:
                Log.d(TAG, "STATUS_SUCCESS ");
                event.dismiss();
                break;

        }
    }


}
