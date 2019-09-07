package com.example.target_club_in_donga.Package_LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;

public class SignUpActivity_01 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_01_agreement);

        // checkBox id 가져오기
        final CheckBox activity_signup_01_checkBox_Agreement_total = findViewById(R.id.activity_signup_01_checkBox_Agreement_total);
        final CheckBox activity_signup_01_checkBox_Agreement_term = findViewById(R.id.activity_signup_01_checkBox_Agreement_term);
        final CheckBox activity_signup_01_checkBox_Agreement_privacy = findViewById(R.id.activity_signup_01_checkBox_Agreement_privacy);

        // Scroll 동작 구현
        TextView activity_signup_01_TextView_Agreement_term_content = findViewById(R.id.activity_signup_01_TextView_Agreement_term_content);
        activity_signup_01_TextView_Agreement_term_content.setMovementMethod(ScrollingMovementMethod.getInstance());

        TextView activity_signup_01_TextView_Agreement_privacy_content = findViewById(R.id.activity_signup_01_TextView_Agreement_privacy_content);
        activity_signup_01_TextView_Agreement_privacy_content.setMovementMethod(ScrollingMovementMethod.getInstance());

        // 취소 버튼 활성화
        Button activity_signup_01_cancel_btn = findViewById(R.id.activity_signup_01_cancel_btn);
        activity_signup_01_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity_01.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 다음페이지 버튼 활성화
        Button activity_signup_01_next_btn = findViewById(R.id.activity_signup_01_next_btn);
        activity_signup_01_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_signup_01_checkBox_Agreement_total.isChecked()) {

                    Intent intent2 = getIntent();
                    String loginIdentity = intent2.getExtras().getString("loginIdentity");
                    if(loginIdentity.equals("email")){
                        Intent intent = new Intent(SignUpActivity_01.this, SignUpActivity_02.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(loginIdentity.equals("google")){
                        Intent intent = new Intent(SignUpActivity_01.this, SignUpActivity_03.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(SignUpActivity_01.this, "동의하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 전체 체크박스 true or false
        activity_signup_01_checkBox_Agreement_total.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_signup_01_checkBox_Agreement_total.isChecked()) {
                    activity_signup_01_checkBox_Agreement_term.setChecked(true);
                    activity_signup_01_checkBox_Agreement_privacy.setChecked(true);
                } else {
                    activity_signup_01_checkBox_Agreement_term.setChecked(false);
                    activity_signup_01_checkBox_Agreement_privacy.setChecked(false);
                }
            }
        });

        // 이용약관 동의 체크박스
        activity_signup_01_checkBox_Agreement_term.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_signup_01_checkBox_Agreement_term.isChecked()) {
                    if (activity_signup_01_checkBox_Agreement_privacy.isChecked()) {
                        activity_signup_01_checkBox_Agreement_total.setChecked(true);
                    }
                } else {
                    activity_signup_01_checkBox_Agreement_total.setChecked(false);
                }
            }
        });

        // 개인정보 수집 체크박스
        activity_signup_01_checkBox_Agreement_privacy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity_signup_01_checkBox_Agreement_privacy.isChecked()) {
                    if (activity_signup_01_checkBox_Agreement_term.isChecked()) {
                        activity_signup_01_checkBox_Agreement_total.setChecked(true);
                    }
                } else {
                    activity_signup_01_checkBox_Agreement_total.setChecked(false);
                }
            }
        });

    }
}
