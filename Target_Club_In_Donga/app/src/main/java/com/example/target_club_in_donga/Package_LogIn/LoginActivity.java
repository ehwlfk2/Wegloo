package com.example.target_club_in_donga.Package_LogIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.example.target_club_in_donga.HomeActivity;
import com.example.target_club_in_donga.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 10;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener; // 로그인했을때 프로세스 실행할거
    EditText activity_login_id_editText, activity_login_pw_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();

        // findById
        Button activity_login_signup_btn = findViewById(R.id.activity_login_signup_btn);
        Button activity_login_login_btn = findViewById(R.id.activity_login_login_btn);
        SignInButton activity_login_google_btn = findViewById(R.id.activity_login_google_btn);
        LoginButton activity_login_facebook_btn = findViewById(R.id.activity_login_facebook_btn);
        // find email, pw
        activity_login_id_editText = findViewById(R.id.activity_login_id_editText);
        activity_login_pw_editText = findViewById(R.id.activity_login_pw_editText);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // request ID Token
                .requestEmail()
                .build();

        //
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 페이스북
        mCallbackManager = CallbackManager.Factory.create();

        // 파이어베이스
        mAuth = FirebaseAuth.getInstance(); // single 톤 패턴으로 작동

        activity_login_signup_btn.setOnClickListener(this);
        activity_login_google_btn.setOnClickListener(this);
        activity_login_facebook_btn.setOnClickListener(this);
        activity_login_login_btn.setOnClickListener(this);

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("develop_check", "페이스북 로그인 성공 : " + loginResult);

                // LoginManager 는 로그인한 사용자의 현재 AccessToken 및 Profile을 설정
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Log.v("develop_check", "loginResult.getAccessToken().toString() : " + loginResult.getAccessToken().toString());
                Log.v("develop_check", "Profile.getCurrentProfile : " + Profile.getCurrentProfile());

                AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("develop_check", "페이스북-파이어베이스 연동 실패");
                            //Toast.makeText(LoginActivity.this, "페이스북 로그인에 문제 발생 010.7152.6215 으로 연락주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("develop_check", "페이스북-파이어베이스 연동 성공");
                            //Toast.makeText(LoginActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                            //success_of_login();
                        }
                    }
                });
                // App code
            }

            @Override
            public void onCancel() {
                Log.w("develop_check", "페이스북 로그인 실패");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("develop_check", "페이스북 로그인 에러 => " + exception);
                // App code
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    database.getReference().child("User").child(user.getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try{
                                int tf = dataSnapshot.getValue(int.class);
                                //Toast.makeText(LoginActivity.this, ""+tf, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                if(tf >= 1){
                                    Toast.makeText(LoginActivity.this, "관리자 로그인", Toast.LENGTH_SHORT).show();
                                    //intent.putExtra("adminCheck",true);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                                    //intent.putExtra("adminCheck",false);
                                }
                                startActivity(intent);
                                finish();
                            }catch (NullPointerException e){

                                Toast.makeText(LoginActivity.this, "구글 페북 처음이시군요?", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SignUpActivity_01.class);
                                intent.putExtra("loginIdentity","google");
                                startActivity(intent);
                                finish();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Toast.makeText(Vote_Login.this, "에러", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // User is signed in
                    //Intent intent = new Intent(LoginActivity.this, HomeActivity_Fragment.class);
                    //finish();
                } else {
                    // User is signed out
                    //Toast.makeText(LoginActivity.this, "이메일 회원가입 해주세요", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };  // mAuthListener


    }   // onCreate

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


    // onActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 페이스북 콜백 함수
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // 구글 로그인 요청
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("develop_check", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("develop_check", "firebaseAuthWithGoogle : " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("develop_check", "signInWithCredential : success");
                    //Toast.makeText(LoginActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    //success_of_login();
                    // FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("develop_check", "signInWithCredential : failure =>", task.getException());
                    //Toast.makeText(LoginActivity.this, "구글 로그인에 문제 발생 010.7152.6215 으로 연락주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(){
        String email = activity_login_id_editText.getText().toString();
        String pw = activity_login_pw_editText.getText().toString();
        if(email.length() != 0 && pw.length() != 0) {
            mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요\n아니면 가입이 안되있을지Do?", Toast.LENGTH_SHORT).show();
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

    private void success_of_login() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity_03.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.activity_login_signup_btn) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity_01.class);
            intent.putExtra("loginIdentity","email");
            startActivity(intent);
            finish();
        } else if (i == R.id.activity_login_google_btn) {
            Log.v("develop_check", "구글 로그인 시도");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else if (i == R.id.activity_login_facebook_btn) {
            Log.v("develop_check", "페이스북 로그인 시도");
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        } else if (i == R.id.activity_login_login_btn) {
            Log.v("develop_check", "로그인 시도");
            loginUser();
            //onStart();
        }
    }   // onClick
}
