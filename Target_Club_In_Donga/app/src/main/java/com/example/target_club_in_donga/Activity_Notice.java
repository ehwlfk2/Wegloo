package com.example.target_club_in_donga;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.target_club_in_donga.ActivityAdapter.ViewPagerAdapter_Notice;
import com.example.target_club_in_donga.Fragments.Fragment_Home;

public class Activity_Notice extends AppCompatActivity implements Fragment_Home.OnFragmentInteractionListener {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ViewPagerAdapter_Notice fragmentAdapter = new ViewPagerAdapter_Notice(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
    }

    // 홈에서 공지사항을 눌었을 떄, viewPager에서 ViewPagerAdapter_Notice로 공지사항화면이 나오고
    // 오른쪽에서 왼쪽으로 슬라이드를 하면 홈 화면이 나오도록 한다.

    @Override
    public void onFragmentInteraction(final Uri uri) {

    }
}
