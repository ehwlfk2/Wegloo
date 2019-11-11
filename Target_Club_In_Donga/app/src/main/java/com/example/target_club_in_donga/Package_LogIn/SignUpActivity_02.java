package com.example.target_club_in_donga.Package_LogIn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class SignUpActivity_02 extends AppCompatActivity implements View.OnClickListener {
    // 주소 받는 변수
    private EditText activity_signup_02_AutoCompleteTextView_email_Address;
    private Button activity_signup_02_send_code_btn;
    private String emailAddress;

    // random 숫자
    private String emailCode;

    // TimeCounter
    private TextView activity_signup_02_timeCounter, activity_signup_02_textview_cnumberError;
    private EditText activity_signup_02_EditText_CNumber;
    private Button activity_signup_02_next_btn;
    private ImageButton activity_signup_02_cancel_btn;
    private CountDownTimer countDownTimer;
    private ImageView signup_02_imageview_guidline_1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private static int ms_StartTimer = 60 * 5 * 1000; // 5분
    private static int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_02_ver0);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        // 변수 선언
        activity_signup_02_AutoCompleteTextView_email_Address = findViewById(R.id.signup_02_edittext_email);
        activity_signup_02_send_code_btn = findViewById(R.id.signup_02_button_certificationsend);
        activity_signup_02_next_btn = findViewById(R.id.signup_02_button_next);
        activity_signup_02_textview_cnumberError = findViewById(R.id.signup_02_textview_error);
        activity_signup_02_EditText_CNumber = findViewById(R.id.signup_02_edittext_certification);
        activity_signup_02_timeCounter = findViewById(R.id.signup_02_textview_timelimit);
        activity_signup_02_cancel_btn = findViewById(R.id.signup_02_button_back);
        // 취소 버튼

        // 인증번호 전송
        activity_signup_02_send_code_btn.setOnClickListener(this);
        activity_signup_02_next_btn.setOnClickListener(this);
        activity_signup_02_cancel_btn.setOnClickListener(this);
        // 이메일 아이디부분 'space', '@' 문자 처리;

    }   // onCreate


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_02_button_certificationsend:
                emailAddress = activity_signup_02_AutoCompleteTextView_email_Address.getText().toString();
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    /**
                     * 원래는 로그인방식을 가져오는 메소드인데
                     * 지금은 그냥 이메일 중복확인으로 씀
                     */
                    mAuth.fetchSignInMethodsForEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();
                                if(signInMethods.size() == 0){
                                    activity_signup_02_AutoCompleteTextView_email_Address.setEnabled(false);
                                    activity_signup_02_EditText_CNumber.requestFocus();

                                    emailCode = createEmailCode();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("email", emailAddress);
                                    map.put("code", emailCode);
                                    database.getReference().child("AppUser").child("TempUser").push().setValue(map);

                                    activity_signup_02_send_code_btn.setVisibility(View.INVISIBLE);
                                    activity_signup_02_EditText_CNumber.setVisibility(View.VISIBLE);
                                    activity_signup_02_timeCounter.setVisibility(View.VISIBLE);
                                    activity_signup_02_next_btn.setVisibility(View.VISIBLE);
                                    countDownTimer();
                                    //Toast.makeText(SignUpActivity_02.this, "아이디 없음", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(SignUpActivity_02.this, "이미 존재하는 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                //Log.e(TAG, "Error getting sign in methods for user", task.getException());
                                //Toast.makeText(SignUpActivity_02.this, "없음", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(SignUpActivity_02.this,"올바른 이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.signup_02_button_next:

                String user_answer = activity_signup_02_EditText_CNumber.getText().toString();
                if (user_answer.equals(emailCode)) {
                    //remove tempUser
                    final ArrayList<String> dbKey = new ArrayList<String>();

                    database.getReference().child("AppUser").child("TempUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dbKey.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                CertificationData data = snapshot.getValue(CertificationData.class);
                                if(data.email.equals(emailAddress))
                                    dbKey.add(snapshot.getKey());
                            }

                            for(int i=0;i<dbKey.size();i++){
                                database.getReference().child("AppUser").child("TempUser").child(dbKey.get(i)).removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Intent intent = new Intent(SignUpActivity_02.this, SignUpActivity_03.class);
                    intent.putExtra("emailAddress",emailAddress);
                    startActivity(intent);
                    finish();
                } else {
                    activity_signup_02_textview_cnumberError.setVisibility(View.VISIBLE);
                    activity_signup_02_textview_cnumberError.setText("인증번호 오류 입니다.");
                }
                break;
            case R.id.signup_02_button_back:
                finish();
                break;
        }   // switch
    }   // onClick

    public void countDownTimer() { //카운트 다운 메소드
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(ms_StartTimer, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long timeCounter) { //(300초에서 1초 마다 계속 줄어듬)

                long Email_Code_timeCount = timeCounter / 1000;
                if ((Email_Code_timeCount - ((Email_Code_timeCount / 60) * 60)) >= 10)
                    activity_signup_02_timeCounter.setText(String.format(getResources().getString(R.string.SignUp_identification_timeCounter),
                            (Email_Code_timeCount / 60), Email_Code_timeCount - ((Email_Code_timeCount / 60) * 60)));
                else
                    activity_signup_02_timeCounter.setText(String.format(getResources().getString(R.string.SignUp_identification_timeCounter_single),
                            (Email_Code_timeCount / 60), Email_Code_timeCount - ((Email_Code_timeCount / 60) * 60)));

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
            }

            @Override
            public void onFinish() { //시간이 다 되면 액티비티 다시띄움
                /*Toast.makeText(SignUpActivity_02.this, "인증번호 입력 시간이 초과하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity_02.this, SignUpActivity_02.class);
                finish();
                startActivity(intent);*/
                /**
                 * 시간다되면 액티비티 띄우려고 했는데 그렇게하니까 시간초 다되면 다른화면에있어도 이화면으로 오더라 고쳐야댐
                 */
            }
        }.start();

    }   // countDownTimer

    private String createEmailCode() {
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        StringBuilder newCode = new StringBuilder();

        for (int x = 0; x < 4; x++) {
            int random = (int) (Math.random() * str.length);
            newCode.append(str[random]);
        }

        return newCode.toString();
    }



    /*
    @Override
    public void onBackPressed() {
        // 시간초를 초기화 하기위한 버튼
        finish();
        super.onBackPressed();
    }*/

}   // Activity class

