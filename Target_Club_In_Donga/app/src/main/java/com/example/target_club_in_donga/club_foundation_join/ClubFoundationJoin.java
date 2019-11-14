package com.example.target_club_in_donga.club_foundation_join;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.target_club_in_donga.Attend.AttendActivity_Admin_Home;
import com.example.target_club_in_donga.Attend.AttendActivity_Home;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.UserDetailActivity;
import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class ClubFoundationJoin extends AppCompatActivity {

    private ConstraintLayout clubFoundation_nextBtn, clubJoin_nextBtn;
    private ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_foundation_join);

        backBtn = findViewById(R.id.joinorcreate_00_button_back);
        clubFoundation_nextBtn = findViewById(R.id.joinorcreate_00_layout_create);
        clubJoin_nextBtn = findViewById(R.id.joinorcreate_00_layout_join);

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

        Button tempViewPagerBtn = findViewById(R.id.tempViewPagerBtn);
        tempViewPagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ClubFoundationJoin.this, HomeActivityView.class);
                startActivity(intent);
                finish();
            }
        });
        Button tempIntentBtn = findViewById(R.id.tempIntentBtn);
        tempIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button fragment_home_btn_1 = (Button) findViewById(R.id.fragment_home_btn_1);
        Button fragment_home_btn_2 = (Button) findViewById(R.id.fragment_home_btn_2);
        Button fragment_home_btn_3 = (Button) findViewById(R.id.fragment_home_btn_3);
        fragment_home_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent  = new Intent(ClubFoundationJoin.this, AttendActivity_Home.class);
                startActivity(intent);
            }
        });
        fragment_home_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent  = new Intent(ClubFoundationJoin.this, UserDetailActivity.class);
                startActivity(intent);
            }
        });
        fragment_home_btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent  = new Intent(ClubFoundationJoin.this, AttendActivity_Admin_Home.class);
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