package com.example.target_club_in_donga.Package_LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.ss.formula.functions.T;

import java.util.regex.Pattern;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class SignUpActivity_03 extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private String emailAddress;
    private EditText activity_signup_03_EditText_email, activity_signup_03_EditText_pw, activity_signup_03_EditText_pwCheck;
    private Button activity_signup_03_next_btn;
    private ImageButton activity_signup_03_cancel_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_03_ver0);

        mAuth = FirebaseAuth.getInstance();
        /*InputFilter filter_mail = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (source.equals("") || ps.matcher(source).matches()) {
                    return source;
                }
                //Toast.makeText(getContext(), "한글, 영문, 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                Toast.makeText(SignUpActivity_03.this, "한글 영문 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                return "";
            }
        };

        activity_signup_03_EditText_name.setFilters(new InputFilter[]{filter_mail});*/

        //(영문(대소문자 구분), 숫자, 특수문자 조합, 9~12자리)



        activity_signup_03_EditText_email = findViewById(R.id.singup_03_textview_emailcheck);
        activity_signup_03_EditText_pw = findViewById(R.id.signup_03_edittext_psw);
        activity_signup_03_EditText_pwCheck = findViewById(R.id.signup_03_edittext_pswcheck);


        // 데이터 수신
        Intent intent = getIntent();
        emailAddress = intent.getExtras().getString("emailAddress");
        activity_signup_03_EditText_email.setText(emailAddress);
        activity_signup_03_EditText_email.setEnabled(false);
        /*
        try {
            // null 에러처리
            emailAddress = intent.getExtras().getString("emailAddress");
            //emailPw = intent.getExtras().getString("emailPw");
            activity_signup_03_EditText_email.setText(String.format(getResources().getString(R.string.email_All), emailSubject, emailAddress));
            activity_signup_03_EditText_pw.setText("* * * * * *");
            activity_signup_03_EditText_pw.setEnabled(false);

            Log.v("develop_check", "email_All : " + emailSubject + "@" + emailAddress);
        } catch (NullPointerException e) {
            activity_signup_03_EditText_email.setText(mAuth.getCurrentUser().getEmail());
            activity_signup_03_EditText_pw.setText("* * * * * *");
            activity_signup_03_EditText_pw.setEnabled(false);
            //Log.e("develop_check", "이메일정보를 intent 하던 중 에러 발생했습니다. : " + e);
        }*/

        // Button
        activity_signup_03_cancel_btn = findViewById(R.id.singup_03_button_back);
        activity_signup_03_next_btn = findViewById(R.id.singup_03_button_next);

        activity_signup_03_cancel_btn.setOnClickListener(this); // 취소 버튼 활성화
        activity_signup_03_next_btn.setOnClickListener(this);   // 다음 버튼 활성화

    }   // onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.singup_03_button_back:
                finish();
                break;
            case R.id.singup_03_button_next:
                String pw = activity_signup_03_EditText_pw.getText().toString();
                String pwCheck = activity_signup_03_EditText_pwCheck.getText().toString();

                if(!pw.equals(pwCheck)) {
                    Toast.makeText(SignUpActivity_03.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$", pw)){ //8자리이상 영문(대소문자 구분) + 숫자 + 특수문자
                    Toast.makeText(SignUpActivity_03.this, "비밀번호 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$", pwCheck)){
                    Toast.makeText(SignUpActivity_03.this, "비밀번호 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(SignUpActivity_03.this, SignUpActivity_04.class);
                    createUser(emailAddress,pwCheck);
                    /*intent.putExtra("loginIdentity","email");
                    intent.putExtra("emailAddress",emailAddress);
                    intent.putExtra("emailPassword",pwCheck);*/
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }   // onClick

    private void createUser(final String emailAddress, final String pw) {
        //LoginData data = new LoginData(name, phone, studentNumber, school, 0);
        mAuth.createUserWithEmailAndPassword(emailAddress,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("develop_check", "createUserWithEmail : success");
                    loginUser(emailAddress, pw);
                    //Toast.makeText(SignUpActivity_03.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SignUpActivity_03.this, ""+mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("develop_check", "createUserWithEmail : failure => ", task.getException());
                    //Toast.makeText(this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SignUpActivity_02.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String pw){
        //String email = activity_login_id_editText.getText().toString();
        //String pw = activity_login_pw_editText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    //Toast.makeText(LoginActivity.this, "이메일 회원가입해주3", Toast.LENGTH_SHORT).show();
                    Log.w("develop_check", "로그인에 실패했습니다.");
                } else {
                    //Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    Log.w("develop_check", "로그인에 성공했습니다.");
                    //success_of_login();

                }
            }
        });
    }
}
