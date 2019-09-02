package com.example.target_club_in_donga.Package_LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD
<<<<<<< HEAD:Target_Club_In_Donga/app/src/main/java/com/example/target_club_in_donga/SignUpActivity_04.java
import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
=======
import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.R;
>>>>>>> 556634f0c71a08376d92731aabbb9255c8ea0b91:Target_Club_In_Donga/app/src/main/java/com/example/target_club_in_donga/Package_LogIn/SignUpActivity_04.java
=======
import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.R;
>>>>>>> 556634f0c71a08376d92731aabbb9255c8ea0b91

public class SignUpActivity_04 extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_04_finish);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
<<<<<<< HEAD
                Intent intent = new Intent(SignUpActivity_04.this, HomeActivity_Fragment.class);
=======
                Intent intent = new Intent(SignUpActivity_04.this, HomeActivity.class);
>>>>>>> 556634f0c71a08376d92731aabbb9255c8ea0b91
                startActivity(intent);
                finish();
            }
        },3000 );   // 3초 딜레이

    }   // onCreate

}   // SignUpActivity_04
