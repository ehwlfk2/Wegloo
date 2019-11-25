package com.example.target_club_in_donga.club_foundation_join;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.target_club_in_donga.R;

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

    }
}
