package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity_04 extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_04_finish);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignUpActivity_04.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },3000 );   // 3초 딜레이

    }   // onCreate

}   // SignUpActivity_04
