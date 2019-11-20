package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;

public class MyInformation extends AppCompatActivity {
    private ImageButton myinfo_back;
    private boolean isRealName, pushOnOff;
    private String myResume;
    private LinearLayout myinfo_user_name_layout, myinfo_profile_Thumbnail_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfomation);

        Intent intent1 = getIntent();
        isRealName = intent1.getExtras().getBoolean("thisClubIsRealName");
        myResume = intent1.getExtras().getString("myResume");
        pushOnOff = intent1.getExtras().getBoolean("pushOnOff");

        myinfo_user_name_layout = findViewById(R.id.myinfo_user_name_layout);
        myinfo_profile_Thumbnail_layout = findViewById(R.id.myinfo_profile_Thumbnail_layout);
        myinfo_back = findViewById(R.id.myinfo_back);

        if(isRealName){
            myinfo_user_name_layout.setVisibility(View.GONE);
            myinfo_profile_Thumbnail_layout.setVisibility(View.GONE);
        }
        if(pushOnOff){

        }
        else{

        }
        myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
