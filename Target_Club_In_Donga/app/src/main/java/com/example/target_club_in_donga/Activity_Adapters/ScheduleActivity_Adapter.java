package com.example.target_club_in_donga.Activity_Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.target_club_in_donga.Fragments.*;

import java.util.ArrayList;

public class ScheduleActivity_Adapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentData;

    public ScheduleActivity_Adapter(final FragmentManager fm) {
        super(fm);
        fragmentData = new ArrayList<>();
        fragmentData.add(new HomeActivity_Fragment());
        fragmentData.add(new ScheduleActivity_Fragment());
    }

    // ArrayList에 Material_Schedule와 Fragment_Home를 추가시킨다.
    // viewPager가 두 개의 Fragment를 묶어서 사용하는데, 일정화면이 왼쪽 홈화면 오른쪽에 위치

    @Override
    public Fragment getItem(int position) {
        return fragmentData.get(position);
    }

    @Override
    public int getCount() {
        return fragmentData.size();
    }
}
