package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.Package_LogIn.Congratulation;
import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.Package_LogIn.SignUpActivity_04;
import com.example.target_club_in_donga.Vote.VoteActivity_Main;

import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    // 앱에 애널리틱스 추가 (1)
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String clubName; //핵심핵심 동아리명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_ver0);

        // 앱에 애널리틱스 추가 (2) Obtain the FirebaseAnalytics instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        // 인터넷 사용을 위한 권한을 허용 (2)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser(); //Auth에 있으면 바아로 자동로그인

                if (user != null) {
                    FirebaseDatabase.getInstance().getReference().child("AppUser").child(user.getUid()).child("recentClub").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) { //DB에 있는아이딘지 없는지 체크
                            String recentClub = dataSnapshot.getValue(String.class);

                            if(recentClub == null){//recentClub이 없을경우
                                Intent intent = new Intent(MainActivity.this, HomeActivityView.class);
                                intent.putExtra("isRecent",false);
                                startActivity(intent);
                                finish();
                            }
                            else{ //recentClub이 있는경우 fcm체크후 보내줌 근데 현재는 FCM 없앨꺼니까 바로 홈으로 땡길꺼임 일단
                                clubName = recentClub;
                                /**
                                 Bundle extras = getIntent().getExtras();
                                 String fcmCheck = "None";
                                 if(extras != null) {
                                 if (extras.containsKey("fcmCheck")) {
                                 fcmCheck = extras.getString("fcmCheck");
                                 }
                                 }

                                 if(fcmCheck.equals("Vote")){
                                 Toast.makeText(MainActivity.this, ""+recentClub, Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(MainActivity.this, VoteActivity_Main.class);
                                 startActivity(intent);
                                 finish();
                                 }
                                 else if(fcmCheck.equals("Notice")){
                                 Toast.makeText(MainActivity.this, ""+recentClub, Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                                 startActivity(intent);
                                 finish();
                                 }
                                 else{
                                 Toast.makeText(MainActivity.this, ""+recentClub, Toast.LENGTH_SHORT).show();
                                 Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                 startActivity(intent);
                                 finish();
                                 }
                                 */
                                //Toast.makeText(MainActivity.this, ""+recentClub, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivityView.class);
                                intent.putExtra("isRecent",true);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Toast.makeText(Vote_Login.this, "에러", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else { //로그인 되있는게 없음
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

    }
    @Override
    public void onStart() {
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
