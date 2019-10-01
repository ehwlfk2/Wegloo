package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.example.target_club_in_donga.Package_LogIn.AppLoginData;
import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.Package_LogIn.SignUpActivity_01;
import com.example.target_club_in_donga.Package_LogIn.SignUpActivity_04;
import com.facebook.login.LoginManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    // 앱에 애널리틱스 추가 (1)  com.google.firebase.analytics.FIrebaseAnalytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; // 로그인했을때 프로세스 실행할거
    public static String clubName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 앱에 대널리틱스 추가 (2) Obtain the FirebaseAnalytics instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        // 인터넷 사용을 위한 권한을 허용 (2)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        /*Button login_btn = findViewById(R.id.activity_main_change_loginPage_btn);

        login_btn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser(); //Auth에 있으면 바아로 자동로그인
                if (user != null) {
                    FirebaseDatabase.getInstance().getReference().child("AppUser").child(user.getUid()).child("recentClub").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) { //DB에 있는아이딘지 없는지 체크
                            try{
                                String recentClub = dataSnapshot.getValue(String.class);
                                if(recentClub == null){
                                    Toast.makeText(MainActivity.this, "recent결정하자", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, SignUpActivity_04.class);
                                    //intent.putExtra("loginIdentity","google");
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    clubName = recentClub;
                                    //Toast.makeText(LoginActivity.this, ""+tf, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, ""+recentClub, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }catch (NullPointerException e){ //recent가없음
                                Toast.makeText(MainActivity.this, "recent결정하자", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, SignUpActivity_04.class);
                                //intent.putExtra("loginIdentity","google");
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Toast.makeText(Vote_Login.this, "에러", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else { //Auth에 없으면 ㅈㅈ
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    // User is signed out
                    //Toast.makeText(MainActivity.this, "이메일 회원가입 해주세요", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };  // mAuthListener

    }
    @Override
    public void onStart() {
        try {
            //mAuth.signOut();
            //LoginManager.getInstance().logOut();
            Log.v("develop_check", "기존 아이디 로그아웃");
        } catch (Exception exception) {
            Log.v("develop_check", "기존 로그인 되어있던게 없습니다. => " + exception);
        }
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

 /*
        Button button = findViewById(R.id.activity_main_size_change_btn);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ImageView img = findViewById(R.id.activity_main_login_screen);
                LayoutParams params = (LayoutParams) img.getLayoutParams();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                params.width = displayMetrics.widthPixels;
                params.height = displayMetrics.heightPixels;

                img.setLayoutParams(params);
            }
        }); // OnClickListener

        // 기기의 해상도 정보를 가져오기 위한 소스 (WindowMangager 객체 이용)
        // getMerics() 함수에 DisplayMetrics 객체를 생성해서 넘긴다
        // DisplayMetrics <- 화면 해상도와 밀도, 스케일링 정보가 있는 객체를 담는다.
        // DisplayMetrics 에서 widthPixels, heightPixels 이 화면 픽셀 정보가 됩니다.
        Display metrics = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        mDisWidth = metrics.getWidth();


        // 리턴받은 DisplayMetrics 정보로 ImageVIew 의 크기를 조절하는 소스
        // ImageView 의 가로 세로 값을 조절하기 위해서 LayoutParams 객체를 리턴
        // 내부에 width, height 속성값을 DisplayMetrics 에 담겨 있는 픽셀 값으로 세팅하면 크기 변경 가능
        ImageView img = (ImageView) findViewById(R.id.activity_main_login_screen);
        LayoutParams params = (LayoutParams) img.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;

*/

