package com.example.target_club_in_donga.home_viewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.target_club_in_donga.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class HomeActivityView extends AppCompatActivity {
    public static MoviePagerAdapter viewAdapter;
    private ViewPager activity_home_viewPager;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
//        mAdView = findViewById(R.id.activity_home_adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        activity_home_viewPager = findViewById(R.id.activity_home_viewPager);
        activity_home_viewPager.setOffscreenPageLimit(3);

        viewAdapter = new MoviePagerAdapter(getSupportFragmentManager());

        ClubSelectedFragment0 clubSelectedFragment0 = new ClubSelectedFragment0();
        viewAdapter.addItem(clubSelectedFragment0);
        HomeFragment0 homeFragment0 = new HomeFragment0();
        viewAdapter.addItem(homeFragment0);

        activity_home_viewPager.setAdapter(viewAdapter);
        activity_home_viewPager.setCurrentItem(1);
    }

    /**
     * 홈으로 intent 는 여기로 다해야대!!! Fragment 로 바로해줄수 없으셈
     */

    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 이미 오른쪽에 다른화면이 붙여져있다면 지우고 붙이게
         * @param item
         */
        public void addItem(Fragment item){
            if(items.size() == 3){
                items.remove(2);
            }
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        /**
         * 화면이동
         */

        public void functionCurrent(){
            activity_home_viewPager.setCurrentItem(2);
        }
    }
}
