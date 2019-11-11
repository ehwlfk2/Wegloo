package com.example.target_club_in_donga;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.target_club_in_donga.Activity_Adapters.TempClubSelectionActivity_Adapter;
import com.example.target_club_in_donga.Fragments.TempClubSelectionActivity_Fragment;

public class TempClubSelectionActivity extends AppCompatActivity implements TempClubSelectionActivity_Fragment.OnFragmentInteractionListener {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        TempClubSelectionActivity_Adapter fragmentAdapter = new TempClubSelectionActivity_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onFragmentInteraction(final Uri uri) {

    }
}
