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
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.target_club_in_donga.Attend.AttendActivity;
import com.example.target_club_in_donga.MainActivity;
import com.example.target_club_in_donga.Notice.NoticeActivity_Main;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;
import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title").toString();
            String text = remoteMessage.getData().get("text").toString();
            String clickAction = remoteMessage.getData().get("clickAction").toString();
            Log.e("click",clickAction);
            sendNotification(title,text,clickAction);
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String title, String text, String clickAction) {
        /*Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);*/
        PendingIntent pendingIntent;
        if(clubName != null){
            Intent intent;
            if(clickAction.equals("Notice")){
                //Log.e("backCheck",backCheck);
                intent = new Intent(this, NoticeActivity_Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else if(clickAction.equals("Vote")){
                //Log.e("backCheck",backCheck);
                intent = new Intent(this, VoteActivity_Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else if(clickAction.equals("AcceptRequest")){
                intent = new Intent(this, HomeActivityView.class);
                intent.putExtra("isRecent",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else if(clickAction.equals("Attend")){
                intent = new Intent(this, AttendActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            else{
                //Log.e("check",check);
                //Log.e("backCheck",backCheck);
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("fcmCheck",clickAction);
            }
            pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        else{
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("fcmCheck",clickAction);
            pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Wegloo";

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_add)
                            .setContentTitle(title)
                            .setContentText(text)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.

            NotificationChannel channel = new NotificationChannel(channelId, "wegloo", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
        else{
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_add)
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
