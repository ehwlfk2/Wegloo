package com.example.target_club_in_donga.Activity_Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.example.target_club_in_donga.Fragments.TempClubSelectionActivity_Fragment;

import java.util.ArrayList;

public class TempClubSelectionActivity_Adapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentData;

    public TempClubSelectionActivity_Adapter (FragmentManager fm) {
        super(fm);
        fragmentData = new ArrayList<>();
        fragmentData.add(new HomeActivity_Fragment());
        fragmentData.add(new TempClubSelectionActivity_Fragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentData.get(position);
    }

    @Override
    public int getCount() {
        return fragmentData.size();
    }
}
