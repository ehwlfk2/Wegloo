package com.example.target_club_in_donga;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.target_club_in_donga.ActivityAdapter.ViewPagerAdapter_Attend;
import com.example.target_club_in_donga.Fragments.Fragment_Home;

public class Activity_Attend extends AppCompatActivity implements Fragment_Home.OnFragmentInteractionListener {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ViewPagerAdapter_Attend fragmentAdapter = new ViewPagerAdapter_Attend(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
    }

    // 메뉴에서 출석을 눌렀을 때, viewPager에서 ViewPagerAdapter_Attend로 출석화면이 나오고
    // 오른쪽에서 왼쪽으로 슬라이드를 하면 홈 화면이 나오도록 한다.

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
