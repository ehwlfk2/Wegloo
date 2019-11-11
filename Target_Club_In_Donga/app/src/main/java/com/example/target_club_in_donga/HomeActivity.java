package com.example.target_club_in_donga;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.target_club_in_donga.Activity_Adapters.HomeActivity_Adapter;
import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class HomeActivity extends AppCompatActivity implements HomeActivity_Fragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private BackPressCloseHandler backPressCloseHandler;

    public interface onKeyBackPressedListener {
        void onBackKey();
    }

    private onKeyBackPressedListener monKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        monKeyBackPressedListener = listener;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        HomeActivity_Adapter fragmentAdapter = new HomeActivity_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }
    // 홈에서 공지사항을 눌었을 떄, viewPager에서 ViewPagerAdapter_Notice로 공지사항화면이 나오고
    // 오른쪽에서 왼쪽으로 슬라이드를 하면 홈 화면이 나오도록 한다.

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
        if (viewPager.getCurrentItem() == 1) {
            backPressCloseHandler.onBackPressed();
        } else {
            if (monKeyBackPressedListener != null) {
                monKeyBackPressedListener.onBackKey();
            } else {
                viewPager.setCurrentItem(1);
//                super.onBackPressed();
            }
        }
    }

    @Override
    public void onFragmentInteraction(final Uri uri) {

    }
}
