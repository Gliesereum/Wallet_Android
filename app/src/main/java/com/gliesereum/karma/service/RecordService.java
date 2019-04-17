package com.gliesereum.karma.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.gliesereum.karma.R;
import com.gliesereum.karma.data.network.json.record.AllRecordResponse;
import com.gliesereum.karma.ui.MapsActivity;
import com.gliesereum.karma.util.FastSave;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

import static com.gliesereum.karma.util.Constants.ACCESS_TOKEN;
import static com.gliesereum.karma.util.Constants.TEST_LOG;
import static com.gliesereum.karma.util.Constants.USER_ID;

//import com.appizona.yehiahd.fastsave.FastSave;

public class RecordService extends Service {

    private NotificationManager notifManager;
    private StompClient mStompClient;
    private String TAG = "TAG";


    public int onStartCommand(Intent intent, int flags, int startId) {
        FastSave.init(getApplicationContext());
        mStompClient = Stomp.over(Stomp.ConnectionProvider.JWS, "wss://dev.gliesereum.com/socket/websocket-app");
        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TEST_LOG, "Stomp connection opened");
                    break;
                case ERROR:
                    Log.e(TEST_LOG, "Error", lifecycleEvent.getException());
//                        startService(new Intent(this, RecordService.class));
                    sendBroadcast(new Intent(this, RestartServiceReceiver.class));
                    break;
                case CLOSED:
                    Log.d(TEST_LOG, "Stomp connection closed");
                    sendBroadcast(new Intent(this, RestartServiceReceiver.class));
//                        startService(new Intent(this, RecordService.class));
                    break;
            }
        });
        Log.d(TEST_LOG, "connect: ");
        mStompClient.connect();
        List<StompHeader> stompHeaders = new ArrayList<>();
        stompHeaders.add(new StompHeader("Authorization", FastSave.getInstance().getString(ACCESS_TOKEN, "")));
        mStompClient.topic("/topic/karma.userRecord." + FastSave.getInstance().getString(USER_ID, ""), stompHeaders).subscribe(topicMessage -> {
            try {
                Log.d("TAG", "jsonObject " + topicMessage.getPayload());
                AllRecordResponse jsonJavaRootObject = new Gson().fromJson(topicMessage.getPayload(), AllRecordResponse.class);
                createNotification("Статус заказа " + jsonJavaRootObject.getStatusRecord(), RecordService.this, jsonJavaRootObject);
            } catch (Exception e) {
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void createNotification(String aMessage, Context context, AllRecordResponse jsonJavaRootObject) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = "default_notification_channel_id"; // default_channel_id
        String title = "default_notification_channel_title"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MapsActivity.class);
//            FastSave.getInstance().saveBoolean("openRecord", true);
//            FastSave.getInstance().saveString("recordId", jsonJavaRootObject.getId());
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MapsActivity.class);
            intent.putExtra("openRecord", true);
            intent.putExtra("recordId", jsonJavaRootObject.getId());
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void onDestroy() {
        Log.d(TEST_LOG, "onDestroy: ");
        super.onDestroy();
        sendBroadcast(new Intent(this, RestartServiceReceiver.class));
    }
}
