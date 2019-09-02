package com.example.target_club_in_donga.ActivityAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.target_club_in_donga.Fragments.*;

import java.util.ArrayList;

public class ViewPagerAdapter_User_Detail extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentData;

    public ViewPagerAdapter_User_Detail(FragmentManager fm) {
        super(fm);
        fragmentData = new ArrayList<>();
        fragmentData.add(new Fragment_Menu_User_Detail());
        fragmentData.add(new Fragment_Home());
    }

    // ArrayList에 Fragment_Attend와 Fragment_Home를 추가시킨다.
    // viewPager가 두 개의 Fragment를 묶어서 사용하는데, 출석화면이 왼쪽 홈화면 오른쪽에 위치

    @Override
    public Fragment getItem(int position) {
        return fragmentData.get(position);
    }

    @Override
    public int getCount() {
        return fragmentData.size();
    }
}
