package com.example.target_club_in_donga.PushMessages;

import com.example.target_club_in_donga.Package_LogIn.LoginData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class SendPushMessages {
    public void multipleSendMessage(final String title, final String text, final String clickAction){
        FirebaseDatabase.getInstance().getReference().child(clubName).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LoginData data = snapshot.getValue(LoginData.class);
                    if(data.isPushAlarmOnOff() && !(snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))){ //자기한텐 안보내기 추가
                        try{
                            SendPushMessages send = new SendPushMessages();
                            send.sendFcm(data.getPushToken(), title,text, clickAction);
                        }catch (NullPointerException e){

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendFcm(String toToken, String title, String text, String clickAction){
        Gson gson = new Gson();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to =  toToken;
        //notificationModel.notification.title = title; //백그라운드
        //notificationModel.notification.text = text;
        //notificationModel.notification.clickAction = clickAction;
        notificationModel.data.title = title; //포그라운드
        notificationModel.data.text = text;
        notificationModel.data.clickAction = clickAction;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AAAAN9u7iok:APA91bHiCw-fGchT3f4FDePrFXNtUQ0PpEBDZOtKuz6Az0x6gMgv2JEhVNcwKeOdJr1UWkX4JBYsShwkU2ZS00CyFNKqSet5JKJOBWxBxzy9Dh_--nbExEbPYWQCU9dwhfSaQqCeOfb3")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }
}
