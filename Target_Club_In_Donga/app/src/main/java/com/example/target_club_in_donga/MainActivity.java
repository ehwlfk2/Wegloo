package com.example.target_club_in_donga;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.Package_LogIn.LoginActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 인터넷 사용을 위한 권한을 허용 (2)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        Button login_btn = findViewById(R.id.activity_main_change_loginPage_btn);

        login_btn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

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

