package com.gliesereum.karma.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;

import static com.gliesereum.karma.util.Constants.TEST_LOG;

public class RestartServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "RestartServiceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TEST_LOG, "onReceive");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TEST_LOG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.O: ");
            Intent intent1 = new Intent(context.getApplicationContext(), RecordService.class);
            context.startForegroundService(intent1);
        }
        Intent intent0 = new Intent(context, RecordService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 111, intent0, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognitionClient activityRecognitionClient = ActivityRecognition.getClient(context);
        activityRecognitionClient.requestActivityUpdates(15, pendingIntent);
    }
}
