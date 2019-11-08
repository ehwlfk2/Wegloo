package com.example.target_club_in_donga.Package_LogIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.target_club_in_donga.MainActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.club_foundation_join.ClubFoundationJoin;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class Congratulation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_05_ver0);

        /*Button tempLogout = findViewById(R.id.tempLogout);
        tempLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Congratulation.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        final Handler handler = new Handler();
        ConstraintLayout signup_05_artborad = findViewById(R.id.signup_05_artborad);
        signup_05_artborad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacksAndMessages(null);
                Intent intent = new Intent(Congratulation.this, ClubFoundationJoin.class);
                startActivity(intent);
                finish();
            }
        });

        Runnable run = new Runnable() //  Runnable 인터페이스를 을 통하여 동작을 구성해야 한다.
        {
            @Override
            public void run() {
                Intent intent = new Intent(Congratulation.this, ClubFoundationJoin.class);
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(run, 3000);
    }
}
