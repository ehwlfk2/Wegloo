package com.example.target_club_in_donga.home_viewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.target_club_in_donga.BackPressCloseHandler;
import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivityView extends AppCompatActivity {
    public static MoviePagerAdapter viewAdapter;
    private ViewPager activity_home_viewPager;
    private AdView mAdView;
    private boolean isRecent;
    private BackPressCloseHandler backPressCloseHandler;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
//        mAdView = findViewById(R.id.activity_home_adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        backPressCloseHandler = new BackPressCloseHandler(this);
        activity_home_viewPager = findViewById(R.id.activity_home_viewPager);
        activity_home_viewPager.setOffscreenPageLimit(3);

        viewAdapter = new MoviePagerAdapter(getSupportFragmentManager());

        Intent intent2 = getIntent();
        isRecent = intent2.getExtras().getBoolean("isRecent");
        if(isRecent){
            ClubSelectedFragment0 clubSelectedFragment0 = new ClubSelectedFragment0();
            viewAdapter.addItem(clubSelectedFragment0);
            HomeFragment0 homeFragment0 = new HomeFragment0();
            viewAdapter.addItem(homeFragment0);
        }
        else{
            ClubSelectedFragment0 clubSelectedFragment0 = new ClubSelectedFragment0();
            viewAdapter.addItem(clubSelectedFragment0);
        }

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

        public void homeCurrent(){
            activity_home_viewPager.setCurrentItem(1);
        }
        public void removeFunction(){
            if(items.size() == 3){
                items.remove(2);
            }
        }
        public void showProgress(String message) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(HomeActivityView.this);
            }
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.show();
        }
        public void dismissDialog() {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        //Log.e("item",+"");
        int count = activity_home_viewPager.getCurrentItem();
        //Log.e("count",count+"");
        if (count == 1) { //어플끄기
            /*FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(HomeActivityView.this, LoginActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();*/
            backPressCloseHandler.onBackPressed();
        }
        else if(count == 0 && isRecent){
            activity_home_viewPager.setCurrentItem(1);
        }
        else if(count == 0){
            //어플끄기
            backPressCloseHandler.onBackPressed();
        }
        else if(count == 2){
            activity_home_viewPager.setCurrentItem(1);
        }
    }
}
