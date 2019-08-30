package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity_03 extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String emailSubject;
    private String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_03_information);

        EditText activity_signup_03_EditText_email = findViewById(R.id.activity_signup_03_EditText_email);
        mAuth = FirebaseAuth.getInstance(); // single 톤 패턴으로 작동

        // 데이터 수신
        Intent intent = getIntent();

        try {   // null 에러처리
            emailSubject = intent.getExtras().getString("emailSubject");
            emailAddress = intent.getExtras().getString("emailAddress");
            activity_signup_03_EditText_email.setText(String.format(getResources().getString(R.string.email_All), emailSubject, emailAddress));
            Log.v("develop_check", "email_All : " + emailSubject + "@" + emailAddress);
        } catch (NullPointerException e) {
            Log.e("develop_check", "이메일정보를 intent 하던 중 에러 발생했습니다. : " + e);
        }

        // Button
        final Button activity_signup_03_cancel_btn = findViewById(R.id.activity_signup_03_cancel_btn);
        Button activity_signup_03_next_btn = findViewById(R.id.activity_signup_03_next_btn);

        activity_signup_03_cancel_btn.setOnClickListener(this); // 취소 버튼 활성화
        activity_signup_03_next_btn.setOnClickListener(this);   // 다음 버튼 활성화

    }   // onCreate


    private void createUser(String emailSubject, String emailAddress, String pw, String name, String phone, String school, String schoolNumber) {
        String email_All = emailSubject + "@" + emailAddress;
        mAuth.createUserWithEmailAndPassword(email_All,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("develop_check", "createUserWithEmail : success");
                            Toast.makeText(SignUpActivity_03.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                            //FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(SignUpActivity_03.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("develop_check", "createUserWithEmail : failure => ", task.getException());
                            Toast.makeText(SignUpActivity_03.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.activity_signup_03_cancel_btn) {
            Intent intent = new Intent(SignUpActivity_03.this, LoginActivity.class);
            startActivity(intent);
        } else if (i == R.id.activity_signup_03_next_btn) {
            String pw = ((EditText) (findViewById(R.id.activity_signup_03_EditText_pw))).getText().toString();
            if(pw.length() >= 6 ) {
                String name = ((EditText) (findViewById(R.id.activity_signup_03_EditText_name))).getText().toString();
                String phone = ((EditText) (findViewById(R.id.activity_signup_03_EditText_phone))).getText().toString();
                String school = ((EditText) (findViewById(R.id.activity_signup_03_EditText_School))).getText().toString();
                String schoolNumber = ((EditText) (findViewById(R.id.activity_signup_03_EditText_school_number))).getText().toString();
                if(!name.isEmpty() && !phone.isEmpty() && !school.isEmpty() && !schoolNumber.isEmpty() ) {
                    createUser(emailSubject, emailAddress, pw, name, phone, school, schoolNumber);
                }
                else{
                    Log.d("develop_check","회원가입란을 모두 채우지 않았습니다..");
                    Toast.makeText(SignUpActivity_03.this, "빈칸이 있어요.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Log.d("develop_check","비밀번호 숫자 제한에 걸렷습니다.");
                Toast.makeText(SignUpActivity_03.this, "비밀번호 숫자 제한에 걸렷습니다.", Toast.LENGTH_SHORT).show();
            }
        }   // 파이어베이스에 회원가입 시도
    }   // onClick
}
