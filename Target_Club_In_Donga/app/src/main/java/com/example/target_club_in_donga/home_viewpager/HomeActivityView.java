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
import android.widget.Toast;

import com.example.target_club_in_donga.BackPressCloseHandler;
import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.club_foundation_join.Join_02_nicName;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.target_club_in_donga.MainActivity.clubName;

public class HomeActivityView extends AppCompatActivity {
    public static MoviePagerAdapter viewAdapter;
    private ViewPager activity_home_viewPager;
    private AdView mAdView;
    private boolean isRecent;
    private BackPressCloseHandler backPressCloseHandler;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);
//        mAdView = findViewById(R.id.activity_home_adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        backPressCloseHandler = new BackPressCloseHandler(this);
        activity_home_viewPager = findViewById(R.id.activity_home_viewPager);
        activity_home_viewPager.setOffscreenPageLimit(3);

        viewAdapter = new MoviePagerAdapter(getSupportFragmentManager());

        Intent intent2 = getIntent();
        isRecent = intent2.getExtras().getBoolean("isRecent");
        if(isRecent){
            viewAdapter.addItem(new ClubSelectedFragment0());
            viewAdapter.addItem(new HomeFragment0());
        }
        else{
            viewAdapter.addItem(new ClubSelectedFragment0());
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
