package com.example.target_club_in_donga;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView schedule_btn, gallery_btn, notice_btn;  // main_home에 있는 텍스트 버튼 (일정, 앨범, 공지사항 순)
    ImageView logo, background, setting_btn; // main_home에 있는 이미지 뷰 (메뉴 밑에 있는 로고, 배경화면 순)
//    Uri backgrounduri; --> 미구현

    private static final int IMAGE_PICK_CODE = 1000; // 갤러리에서 이미지를 받아오기 위한 세가지 변수
    private static final int IMAGE_PICK_CODE2 = 1002; //
    private static final int PERMISSION_CODE = 1001; //

    private TextView nameTextView;
    private TextView emailTextView;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        notice_btn = (TextView)findViewById(R.id.notice_btn);
        schedule_btn = (TextView) findViewById(R.id.schedule);
        gallery_btn = (TextView) findViewById(R.id.gallery);

        background = (ImageView) findViewById(R.id.background);
        setting_btn = (ImageView) findViewById(R.id.setting_btn);
        logo = (ImageView) findViewById(R.id.logo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        nameTextView = view.findViewById(R.id.header_name_textView);
        emailTextView = view.findViewById(R.id.header_email_textView);

        nameTextView.setText(auth.getCurrentUser().getDisplayName());
        emailTextView.setText(auth.getCurrentUser().getEmail());


        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(HomeActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        }); // 공지사항을 누르면 main_home 에서 activity_notice로 activity를 바꿈

        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(HomeActivity.this, ScheduleActivity.class);
                Toast.makeText(getApplicationContext(), "눌렀어요", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }); // 일정을 누르면 main_home 에서 activity_schedule로 activity를 바꿈

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        }); // 앨범을 누르면 폰에 있는 갤러리로 들어가서 이미지를 선택하면 로고로 설정됨 (코드는 잘 모름)

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery2();
                    }
                } else {
                    pickImageFromGallery2();
                }
            }
        }); // main_home에 있는 setting_btn을 누르면 폰에 있는 갤러리로 들어가서 이미지를 선택하면 배경화면으로 설정됨 (코드는 잘 모름)

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the fragment_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

/*        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else */if (id == R.id.nav_logout){
            auth.signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    } // 로고를 바꾸기 위해 필요한 함수 상수변수에 대한 값 1000 가져옴 (코드는 자세히 모름)

    private void pickImageFromGallery2() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE2);
    } // 배경화면를 바꾸기 위해 필요한 함수 상수변수에 대한 값 1002 가져옴 (코드는 자세히 모름)

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    } // 코드는 자세히는 모르나 처음 갤러리로 접근 하였을 떄, 접근을 허용하면 갤러리로 들어가지고 허용 하지 않으면 Permission denied 이라고 밑에 띄어줌

   /*
   public void onChooseaFile(View v) {
        CropImage.activity().start(Activity_Home.this);
    }

    미구현 onActivityResult에서 주석된 부분도 포함

    */

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            logo.setImageURI(data.getData());
//                backgrounduri = result.getUri();
//                logo.setImageURI(backgrounduri);
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE2) {
            background.setImageURI(data.getData());
        }
//        }
    } // 위에 있는 변수값을 가져와서 앨범을 누르면 pickImageFromGallery() 실행되어 값 1000 들어오고 if문에 걸려 로고가 바뀌고,
    // background 이미지 뷰를 누르면 pickImageFromGallery2() 실행되어 값 1002 들어와서 else if문에 걸려 배경화면이 바뀐다.
}
