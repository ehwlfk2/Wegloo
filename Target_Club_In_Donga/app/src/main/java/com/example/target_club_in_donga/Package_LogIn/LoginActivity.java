package com.example.target_club_in_donga.Package_LogIn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.target_club_in_donga.home_viewpager.HomeActivityView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
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
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class LoginActivity extends Activity implements View.OnClickListener {

    private SessionCallback callback;      //콜백 선언
    private FirebaseFunctions mFunctions;
    private com.kakao.usermgmt.LoginButton btn_kakao;
    //유저프로필
    String token = "";
    String name = "";
    private static final int RC_SIGN_IN = 10;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener; // 로그인했을때 프로세스 실행할거
    EditText activity_login_id_editText, activity_login_pw_editText;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ver0);
        progressDialog = new ProgressDialog(this);
        mFunctions = FirebaseFunctions.getInstance();
        database = FirebaseDatabase.getInstance();
        //카카오 로그인 콜백받기
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        loadShared();
        // findById
        TextView activity_login_signup_btn = findViewById(R.id.login_button_signup);
        Button activity_login_login_btn = findViewById(R.id.login_button_login);
        ImageView activity_login_google_btn = findViewById(R.id.login_button_google);
        ImageView activity_login_facebook_btn = findViewById(R.id.login_button_facebook);
        ImageView activity_login_kakao_btn = findViewById(R.id.login_button_kakao);
        TextView reset_id = findViewById(R.id.reset_id_pw);
        btn_kakao = findViewById(R.id.oringin_kakao);
        // find email, pw
        activity_login_id_editText = findViewById(R.id.login_edittext_id);
        activity_login_pw_editText = findViewById(R.id.login_edittext_psw);

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
        activity_login_kakao_btn.setOnClickListener(this);
        reset_id.setOnClickListener(this);


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

                    database.getReference().child("AppUser").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) { //DB에 있는아이딘지 없는지 체크
                            progressDialog.dismiss();
                            activity_login_id_editText.setText("");
                            activity_login_pw_editText.setText("");
                            try{
                                AppLoginData appLoginData = dataSnapshot.getValue(AppLoginData.class);
                                if(appLoginData.getRecentClub() == null){
                                    Intent intent = new Intent(LoginActivity.this, HomeActivityView.class);
                                    intent.putExtra("isRecent",false);
                                    startActivity(intent);
                                }
                                else{
                                    clubName = appLoginData.getRecentClub();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivityView.class);
                                    intent.putExtra("isRecent",true);
                                    startActivity(intent);
                                }

                                /**
                                 * 임시로 getPhone해둔거임임임
                                 */
//                                Intent intent = new Intent(LoginActivity.this, Congratulation.class);
//                                startActivity(intent);
                                //finish();
                            }catch (NullPointerException e){ //auth에는 있는데 db엔 없는경우지 이게 아마?
                                Intent intent = new Intent(LoginActivity.this, SignUpActivity_04.class);
                                intent.putExtra("loginIdentity","notEmail");
                                startActivity(intent);
                                //finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Toast.makeText(Vote_Login.this, "에러", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    // onActivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
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
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
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
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.login_button_signup) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity_01.class);
            intent.putExtra("loginIdentity","email");
            startActivity(intent);
            //finish();
        } else if (i == R.id.login_button_google) {
            progressDialog.setMessage("로그인 중입니다...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.v("develop_check", "구글 로그인 시도");
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);

        } else if (i == R.id.login_button_facebook) {
            progressDialog.setMessage("로그인 중입니다...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.v("develop_check", "페이스북 로그인 시도");
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        } else if (i == R.id.login_button_login) {
            progressDialog.setMessage("로그인 중입니다...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.v("develop_check", "로그인 시도");
            loginUser();
            //onStart();
        } else if (i == R.id.login_button_kakao){
            progressDialog.setMessage("로그인 중입니다...");
            progressDialog.show();
            Log.v("develop_check", "카카오톡 로그인 시도");
            btn_kakao.performClick();
        } else if (i == R.id.reset_id_pw){
            Intent intent = new Intent(getApplicationContext(), Reset_id_pw.class);
            startActivity(intent);
        }
    }   // onClick


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(final UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);
                Log.d("Kakao : ", "유저가입성공");
                // Create a new user with a first and last name
                // 유저 카카오톡 아이디 디비에 넣음(첫가입인 경우에만 디비에저장)
                final String ktoken = Session.getCurrentSession().getAccessToken();
                //String refresh = Session.getCurrentSession().getRefreshToken();
                kakaoCustomAuth(ktoken).continueWithTask(new Continuation<String, Task<AuthResult>>() {
                    @Override
                    public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
                        String firebaseToken = task.getResult();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        return auth.signInWithCustomToken(firebaseToken);
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if( currentUser != null){
                                //
                            }
                            else{
                                //
                            }*/
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to create a Firebase user.", Toast.LENGTH_LONG).show();
                            if (task.getException() != null) {
                                Log.e("tag", task.getException().toString());
                            }
                        }
                    }
                });
            }
        });
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    /*쉐어드에 입력값 저장*/
    private void saveShared( String id, String name) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", id);
        editor.putString("name", name);
        editor.apply();
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getString("token", "");
        name = pref.getString("name", "");
    }

    // [START function_kakaoCustomAuth]
    private  Task<String> kakaoCustomAuth(String ktoken){
        return mFunctions
                .getHttpsCallable("kakaoCustomAuth")
                .call(ktoken)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        try {
                            Map<String, String> jwt = new HashMap<>();
                            jwt = (Map<String, String>) task.getResult().getData();
                            String firebsae_jwt = (String)jwt.get("firebase_token");
                            return firebsae_jwt;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    }
                });
    }
    // [END function_kakaoCustomAuth]
}
