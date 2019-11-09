package com.example.target_club_in_donga.club_foundation_join;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.target_club_in_donga.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class ClubFoundationJoin extends AppCompatActivity {

    private Button backBtn, clubFoundation_nextBtn, clubJoin_nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_foundation_join);

        backBtn = findViewById(R.id.backBtn);
        clubFoundation_nextBtn = findViewById(R.id.clubFoundation_nextBtn);
        clubJoin_nextBtn = findViewById(R.id.clubJoin_nextBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clubFoundation_nextBtn.setOnClickListener(new View.OnClickListener() { //클럽 창설
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ClubFoundationJoin.this,Foundation_01.class);
                startActivity(intent);
            }
        });
        clubJoin_nextBtn.setOnClickListener(new View.OnClickListener() { //클럽 가입
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ClubFoundationJoin.this,Join_01.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        finish();
        super.onBackPressed();
    }
}
