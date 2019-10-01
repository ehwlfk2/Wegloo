package com.example.target_club_in_donga.Package_LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class SignUpActivity_04 extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_04_finish);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignUpActivity_04.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },3000 );   // 3초 딜레이*/

        final Button btn_tcid = findViewById(R.id.btn_tcid);
        btn_tcid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubName = btn_tcid.getText().toString();
                dbdb();
                Intent intent = new Intent(SignUpActivity_04.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Button btn_ka = findViewById(R.id.btn_ka);
        btn_ka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubName = btn_ka.getText().toString();
                dbdb();
                Intent intent = new Intent(SignUpActivity_04.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }   // onCreate

    public void dbdb(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("AppUser").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppLoginData appLoginData = dataSnapshot.getValue(AppLoginData.class);
                LoginData data  = new LoginData();
                data.setPhone(appLoginData.getPhone());
                data.setName(appLoginData.getName());
                data.setAdmin(3);
                data.setPushAlarmOnOff(true);
                FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(uid).setValue(data);
                //Map<String, Object> signUpClub  = new HashMap<>();
                //signUpClub.put(clubName,false);
                FirebaseDatabase.getInstance().getReference().child("AppUser").child(uid).child("signUpClub").child(clubName).setValue(false);
                FirebaseDatabase.getInstance().getReference().child("AppUser").child(uid).child("recentClub").setValue(clubName);
                passPushTokenToServer();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);
        FirebaseDatabase.getInstance().getReference().child(clubName).child("User").child(uid).updateChildren(map);
    }

}   // SignUpActivity_04
