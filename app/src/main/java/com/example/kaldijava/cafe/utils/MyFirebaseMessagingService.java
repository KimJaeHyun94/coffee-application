package com.example.kaldijava.cafe.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.kaldijava.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import marty_library.ration.com.library.utils.MDEBUG;

import static com.example.kaldijava.cafe.utils.Props.FCM_TOKEN_KEY;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        

        MDEBUG.debug("NOTIFIVATION !! " + remoteMessage.toString());

        if (remoteMessage.getData() != null){
            MDEBUG.debug("NOTIFIVATION DATA title !! " + remoteMessage.getData().get("title"));
            MDEBUG.debug("NOTIFIVATION DATA contents!! " + remoteMessage.getData().get("contents"));
            sendNotification(remoteMessage);
        }else if (remoteMessage.getNotification() != null){
            MDEBUG.debug("NOTIFIVATION NOTI TITLE " + remoteMessage.getNotification().getTitle());
            MDEBUG.debug("NOTIFIVATION NOTI BODY !! " + remoteMessage.getNotification().getBody());

        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);

        sharedPreferences.edit().putString(FCM_TOKEN_KEY,s).commit();

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("contents");

        /**
         * 오레오 버전부터는 Notification Channel이 없으면 푸시가 생성되지 않는 현상이 있습니다.
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel = "Kalid";
            String channel_nm = "Kalid_default";

            NotificationManager notichannel = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channelMessage.setDescription("채널에 대한 설명.");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            notichannel.createNotificationChannel(channelMessage);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setChannelId(channel)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());

        } else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());

        }
    }


}
