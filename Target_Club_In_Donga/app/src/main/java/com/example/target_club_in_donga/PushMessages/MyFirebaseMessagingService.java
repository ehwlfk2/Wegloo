package com.example.target_club_in_donga.PushMessages;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.target_club_in_donga.MainActivity;
import com.example.target_club_in_donga.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title").toString();
            String text = remoteMessage.getData().get("text").toString();
            sendNotification(title,text);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String title, String text) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "TCID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_accountbook)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.

            NotificationChannel channel = new NotificationChannel(channelId, "TCID", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
        else{
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_accountbook)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.

            //NotificationChannel channel = new NotificationChannel(channelId, "TICD", NotificationManager.IMPORTANCE_DEFAULT);
            //notificationManager.createNotificationChannel(channel);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }


    }


}
