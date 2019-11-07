package com.example.target_club_in_donga.Package_LogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.target_club_in_donga.MainActivity;
import com.example.target_club_in_donga.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class Congratulation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_05_ver0);

        Button tempLogout = findViewById(R.id.tempLogout);
        tempLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(Congratulation.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
