package com.gliesereum.coupler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gliesereum.coupler.ui.RecordListActivity;
import com.gliesereum.coupler.ui.SingleRecordActivity;
import com.gliesereum.coupler.ui.SplashActivity;
import com.gliesereum.coupler.util.FastSave;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.gliesereum.coupler.util.Constants.RECORD_LIST_ACTIVITY;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG = "TAG_NOTIF";
    private NotificationManager notifManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        FastSave.init(getApplicationContext());
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Data: " + remoteMessage.getData());
//        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"), remoteMessage.getData().get("event"));
        if (FastSave.getInstance().getBoolean(RECORD_LIST_ACTIVITY, false)) {
            Intent intent = new Intent(this, RecordListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            sendNotification(remoteMessage);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        Log.d(TAG, "Long lived task is done.");

        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
//        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * <p>
     * //     * @param messageBody FCM message body received.
     *
     * @param remoteMessage
     */
    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent;
        switch (remoteMessage.getData().get("event")) {
            case "KARMA_USER_RECORD":
                intent = new Intent(this, SingleRecordActivity.class);
                intent.putExtra("objectId", remoteMessage.getData().get("objectId"));
                break;
            case "KARMA_USER_REMIND_RECORD":
                intent = new Intent(this, SingleRecordActivity.class);
                intent.putExtra("objectId", remoteMessage.getData().get("objectId"));
                break;
            default:
                intent = new Intent(this, SplashActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_notification_channel_id";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_directions_car_black_24dp);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_coupler)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.MAGENTA, 500, 1000)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}
