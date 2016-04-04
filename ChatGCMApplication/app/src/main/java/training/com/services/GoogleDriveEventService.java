package training.com.services;

import android.util.Log;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.events.CompletionEvent;

import training.com.common.AppConfig;

/**
 * Created by hawk on 01/04/2016.
 */
public class GoogleDriveEventService extends com.google.android.gms.drive.events.DriveEventService {
    private static final String TAG = "_X_";

    @Override
    public void onCompletion(CompletionEvent event) {
        super.onCompletion(event);
        DriveId driveId = event.getDriveId();
        String FileId = driveId.getResourceId();
        Log.d(TAG, "onComplete: " + driveId);
        Log.i("WTF", FileId);
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
