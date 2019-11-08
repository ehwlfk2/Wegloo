package com.example.target_club_in_donga.Package_LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.target_club_in_donga.R;

public class SignUpActivity_01 extends AppCompatActivity {
    private CheckBox activity_signup_01_checkBox_Agreement_total,activity_signup_01_checkBox_Agreement_term,activity_signup_01_checkBox_Agreement_privacy;
    private ConstraintLayout activity_signup_01_layout_term, activity_signup_01_layout_privacy, activity_signup_01_layout_total;
    private Button activity_signup_01_next_btn;
    private ImageButton activity_signup_01_cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_01_ver0);

        // checkBox id 가져오기

        activity_signup_01_checkBox_Agreement_total = findViewById(R.id.signup_01_checkbox_total);
        activity_signup_01_checkBox_Agreement_term = findViewById(R.id.signup_01_checkbox_term);
        activity_signup_01_checkBox_Agreement_privacy = findViewById(R.id.signup_01_checkbox_privacy);

        //레이아웃 id
        activity_signup_01_layout_term = findViewById(R.id.signup_01_layout_term_plus);
        activity_signup_01_layout_privacy = findViewById(R.id.signup_01_layout_privacy_plus);
        activity_signup_01_layout_total = findViewById(R.id.signup_01_layout_total);

        activity_signup_01_layout_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreementDialog(1);
            }
        });

        activity_signup_01_layout_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreementDialog(2);
            }
        });

        // 취소 버튼 활성화
        activity_signup_01_cancel_btn = findViewById(R.id.singup_01_button_back);
        activity_signup_01_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 다음페이지 버튼 활성화
        activity_signup_01_next_btn = findViewById(R.id.singup_01_button_next);
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
                    else if(loginIdentity.equals("notEmail")){
                        Intent intent = new Intent(SignUpActivity_01.this, SignUpActivity_04.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(SignUpActivity_01.this, "동의하셔야 합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 토탈뷰 체크박스 true or false
        activity_signup_01_layout_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activity_signup_01_checkBox_Agreement_total.isChecked()) {
                    activity_signup_01_checkBox_Agreement_term.setChecked(true);
                    activity_signup_01_checkBox_Agreement_privacy.setChecked(true);
                    activity_signup_01_checkBox_Agreement_total.setChecked(true);
                } else {
                    activity_signup_01_checkBox_Agreement_term.setChecked(false);
                    activity_signup_01_checkBox_Agreement_privacy.setChecked(false);
                    activity_signup_01_checkBox_Agreement_total.setChecked(false);
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
    private void agreementDialog(int flag){ //1은 term 2는 privacy
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

        final View view2 = LayoutInflater.from(this).inflate(R.layout.dialog_signup_01_agreement, null, false);
        builder2.setView(view2);
        final AlertDialog dialog2 = builder2.create();

        Button confirmBtn = view2.findViewById(R.id.dialog_agreement_confirmBtn);
        TextView activity_signup_01_TextView_Agreement_term_content = view2.findViewById(R.id.dialog_agreement_contents);
        TextView activity_signup_01_TextView_text = view2.findViewById(R.id.dialog_agreement_text);
        activity_signup_01_TextView_Agreement_term_content.setMovementMethod(new ScrollingMovementMethod());

        if(flag == 1){
            activity_signup_01_TextView_text.setText(R.string.SignUp_Agreement_term);
            activity_signup_01_TextView_Agreement_term_content.setText(R.string.SignUp_Agreement_term_Contents);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog2.dismiss();
                    activity_signup_01_checkBox_Agreement_term.setChecked(true);
                    if(activity_signup_01_checkBox_Agreement_privacy.isChecked())
                        activity_signup_01_checkBox_Agreement_total.setChecked(true);
                }
            });
        }
        else if(flag == 2){
            activity_signup_01_TextView_text.setText(R.string.SignUp_Agreement_privacy);
            activity_signup_01_TextView_Agreement_term_content.setText(R.string.SignUp_Agreement_privacy_Contents);
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog2.dismiss();
                    activity_signup_01_checkBox_Agreement_privacy.setChecked(true);
                    if(activity_signup_01_checkBox_Agreement_term.isChecked())
                        activity_signup_01_checkBox_Agreement_total.setChecked(true);
                }
            });
        }
        //activity_signup_01_TextView_Agreement_term_content.setVerticalScrollBarEnabled(true);


        //final ImageView exportBtn = view2.findViewById(R.id.account_main_image_dialog_imageview_export);
        //final ImageView imageView = view2.findViewById(R.id.account_main_image_dialog_imageview);

        //Toast.makeText(this, ""+imageUrl, Toast.LENGTH_SHORT).show();
        dialog2.show();
    }
}
