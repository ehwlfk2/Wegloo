package com.example.target_club_in_donga.Package_LogIn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity_02 extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnCancelListener {
    // 이메일 자동 완성 (1)
    String[] email_address = {"naver.com", "google.com", "gmail.com", "donga.ac.kr", "daum.net", "hanmail.net", "yahoo.com", "nate.com"};

    // 주소 받는 변수
    EditText activity_signup_02_EditText_email_Subjuct, activity_signup_02_EditText_pw;
    AutoCompleteTextView activity_signup_02_AutoCompleteTextView_email_Address;
    Button activity_signup_02_send_code_btn;
    String emailSubject, emailAddress;

    // random 숫자
    String emailCode;

    // dialog 변수 선언
    LayoutInflater dialog; // LayoutInflater
    View dialogLayout; // layout을 담을 View
    Dialog signupDialog; // dialog 객체

    // TimeCounter
    TextView activity_signup_02_timeCounter;
    EditText activity_signup_02_EditText_insert_code;
    Button activity_signup_02_check_code_btn;
    CountDownTimer countDownTimer;

    FirebaseAuth mAuth;
    int ms_StartTimer = 60 * 5 * 1000; // 5분
    int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_02_identification);

        mAuth = FirebaseAuth.getInstance();
        // 변수 선언
        activity_signup_02_EditText_email_Subjuct = findViewById(R.id.activity_signup_02_EditText_email_Subjuct);
        activity_signup_02_AutoCompleteTextView_email_Address = findViewById(R.id.activity_signup_02_AutoCompleteTextView_email_Address);
        activity_signup_02_send_code_btn = findViewById(R.id.activity_signup_02_send_code_btn);
        activity_signup_02_EditText_pw = findViewById(R.id.activity_signup_02_EditText_pw);
        // 취소 버튼
        final Button activity_signup_02_cancel_btn = findViewById(R.id.activity_signup_02_cancel_btn);
        activity_signup_02_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity_02.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 다음페이지 활성화

        Button activity_signup_02_next_btn = findViewById(R.id.activity_signup_02_next_btn);
        activity_signup_02_next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activity_signup_02_EditText_pw.getText().toString().length() >= 6){
                    createUser(emailSubject,emailAddress,activity_signup_02_EditText_pw.getText().toString());

                    Intent intent = new Intent(SignUpActivity_02.this, SignUpActivity_03.class);
                    intent.putExtra("emailSubject",emailSubject);
                    intent.putExtra("emailAddress",emailAddress);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(SignUpActivity_02.this, "비밀번호 제대로 입력해 시불", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 인증번호 전송
        activity_signup_02_send_code_btn.setOnClickListener(this);

        // 이메일 아이디부분 'space', '@' 문자 처리;
        activity_signup_02_EditText_email_Subjuct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//                Log.d("develop_check", "It's beforeTextChanged");
//                Log.d("develop_check", "charSequence : " + charSequence);
//                Log.d("develop_check", "start : " + start);
//                Log.d("develop_check", "count : " + count);
//                Log.d("develop_check", "after  : " + after);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                Log.i("develop_check", "It's onTextChanged");
//                Log.i("develop_check", "charSequence : " + charSequence);
//                Log.i("develop_check", "start : " + start);
//                Log.i("develop_check", "before : " + before);
//                Log.i("develop_check", "count : " + count);
                if (count != 0 && charSequence.charAt(start + count - 1) == ' ') {
                    Toast.makeText(SignUpActivity_02.this, "space 를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    activity_signup_02_EditText_email_Subjuct.setText(charSequence.toString().replaceAll(" ", ""));
                    activity_signup_02_EditText_email_Subjuct.setSelection(charSequence.length() - 1);
                }
                if (count != 0 && charSequence.charAt(start + count - 1) == '@') {
                    activity_signup_02_EditText_email_Subjuct.setText(charSequence.toString().replaceAll("@", ""));
                    activity_signup_02_AutoCompleteTextView_email_Address.requestFocus();
                    activity_signup_02_AutoCompleteTextView_email_Address.setText("");
                }

//                Log.d("develop_check", "onTextChanged 종료!!");
            }

            @Override
            public void afterTextChanged(Editable charSequence) {
//                Log.v("develop_check", "It's afterTextChanged");
                Log.v("develop_check", "charSequence : " + charSequence);
            }
        });

        // 이메일 자동 완성 (2)
        activity_signup_02_AutoCompleteTextView_email_Address.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, email_address));
    }   // onCreate

    public void countDownTimer() { //카운트 다운 메소드

        activity_signup_02_timeCounter = dialogLayout.findViewById(R.id.activity_signup_02_timeCounter);
        activity_signup_02_EditText_insert_code = dialogLayout.findViewById(R.id.activity_signup_02_EditText_insert_code);
        activity_signup_02_check_code_btn = dialogLayout.findViewById(R.id.activity_signup_02_check_code_btn);


        countDownTimer = new CountDownTimer(ms_StartTimer, COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long timeCounter) { //(300초에서 1초 마다 계속 줄어듬)

                long Email_Code_timeCount = timeCounter / 1000;
//                Log.d("develop_check", Email_Code_timeCount + "");

                //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                //초가 10보다 크면 그냥 출력
                if ((Email_Code_timeCount - ((Email_Code_timeCount / 60) * 60)) >= 10) {
                    activity_signup_02_timeCounter.setText(String.format(getResources().getString(R.string.SignUp_identification_timeCounter),
                            (Email_Code_timeCount / 60), Email_Code_timeCount - ((Email_Code_timeCount / 60) * 60)));
                } else
                    activity_signup_02_timeCounter.setText(String.format(getResources().getString(R.string.SignUp_identification_timeCounter_single),
                            (Email_Code_timeCount / 60), Email_Code_timeCount - ((Email_Code_timeCount / 60) * 60)));

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
            }

            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
                signupDialog.cancel();
            }
        }.start();

        activity_signup_02_check_code_btn.setOnClickListener(this);

    }   // countDownTimer

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_signup_02_send_code_btn:
                if (activity_signup_02_EditText_email_Subjuct.getText().length() != 0 && activity_signup_02_AutoCompleteTextView_email_Address.getText().length() != 0) {
                    emailSubject = activity_signup_02_EditText_email_Subjuct.getText().toString();
                    emailAddress = activity_signup_02_AutoCompleteTextView_email_Address.getText().toString();
                    emailCode = createEmailCode();
                    Log.v("develop_check","emailCode : " + emailCode);

                    // 이메일 자동 완성 (1)
                    try {
                        Log.v("develop_check", "이메일 전송 시도");
                        GMailSender sender = new GMailSender("1334381@donga.ac.kr", "thdeh@0408");
                        sender.sendMail(getString(R.string.SignUp_email_code_subject_content),
                                String.format(getResources().getString(R.string.SignUp_email_code_body_content),emailCode),
                                "1334381@donga.ac.kr",
                                emailSubject+"@"+emailAddress);
                        Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("develo_check", "이메일 전송에 에러 발생 ; " + e.toString());
                    }

                    dialog = LayoutInflater.from(this);
                    dialogLayout = dialog.inflate(R.layout.activity_signup_02_dialog, null); // LayoutInflater 를 통해 XML 에 정의된 Resource 들을 View 의 형태로 반환 시켜 줌
                    signupDialog = new Dialog(this); // Dialog 객체 생성

                /*
                // 현재의 디스플레이 크기를 받아서 size 에 저장
                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);

                // dialog size 조정
                signupDialog.getWindow().getAttributes().width = (int)(size.x * 0.8f);
                signupDialog.getWindow().getAttributes().height = (int)(size.y * 0.8f);
                */

                    signupDialog.setContentView(dialogLayout); // Dialog 에 inflate 한 View 를 탑재 하여줌
                    signupDialog.setCanceledOnTouchOutside(false); // Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
                    signupDialog.setOnCancelListener(this); // 다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
                    signupDialog.show(); // Dialog 를 나타내어 준다.
                    countDownTimer();
                } else {
                    Toast.makeText(this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.activity_signup_02_check_code_btn: // 다이얼로그 내의 인증번호 확인 버튼을 눌렀을 시
                if (activity_signup_02_EditText_insert_code.getText().length() != 0) {
                    String user_answer = activity_signup_02_EditText_insert_code.getText().toString();
                    if (user_answer.equals(emailCode)) {
                        Toast.makeText(this, "이메일 인증 성공!!\n비밀번호 입력해줘요", Toast.LENGTH_SHORT).show();

                        /*activity_signup_02_send_code_btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(activity_signup_02_EditText_insert_pw.getText().toString().length() >= 6){
                                    Toast.makeText(SignUpActivity_02.this, "비밀번호 다시치셈", Toast.LENGTH_SHORT).show();
                                    activity_signup_02_EditText_insert_pw.setText("");
                                }
                                else{
                                    createUser(emailSubject, emailAddress, activity_signup_02_EditText_insert_pw.getText().toString());

                                    Intent intent = new Intent(SignUpActivity_02.this, SignUpActivity_03.class);
                                    intent.putExtra("emailSubject",emailSubject);
                                    intent.putExtra("emailAddress",emailAddress);
                                    //intent.putExtra("emailPw",activity_signup_02_EditText_insert_pw.getText().toString());
                                    startActivity(intent);
                                }

                                signupDialog.cancel();
                            }
                        });*/
                        activity_signup_02_EditText_pw.setVisibility(View.VISIBLE);
                        activity_signup_02_send_code_btn.setEnabled(false);
                        activity_signup_02_EditText_email_Subjuct.setEnabled(false);
                        activity_signup_02_AutoCompleteTextView_email_Address.setEnabled(false);
                        signupDialog.cancel();


                    } else {
                        Toast.makeText(this, "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "인증번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }   // switch
    }   // onClick

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        countDownTimer.cancel();
    }

    private String createEmailCode() {
        String[] str = {/*"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", */
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        StringBuilder newCode = new StringBuilder();

        for (int x = 0; x < 4; x++) {
            int random = (int) (Math.random() * str.length);
            newCode.append(str[random]);
        }

        return newCode.toString();
    }

    @Override
    public void onBackPressed() {
        // 시간초를 초기화 하기위한 버튼
        finish();
        super.onBackPressed();
    }

    private void createUser(String emailSubject, String emailAddress, final String pw) {
        final String email_All = emailSubject + "@" + emailAddress;
        //LoginData data = new LoginData(name, phone, studentNumber, school, 0);
        mAuth.createUserWithEmailAndPassword(email_All,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("develop_check", "createUserWithEmail : success");
                    loginUser(email_All, pw);
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
        if(email.length() != 0 && pw.length() != 0) {
            mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpActivity_02.this, "아이디와 비밀번호를 확인해주세요\n아니면 가입이 안되있을지Do?", Toast.LENGTH_SHORT).show();
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
        else{
            Log.w("develop_check","아이디와 비밀번호를 입력하지 않았습니다.");
            //Toast.makeText(LoginActivity.this,"아이디와 비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
    }
}   // Activity class
