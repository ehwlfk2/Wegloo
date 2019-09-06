package com.example.target_club_in_donga.Schedule;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.target_club_in_donga.Activity_Adapters.ScheduleActivity_Adapter;
import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;
import com.example.target_club_in_donga.R;

import com.example.target_club_in_donga.Schedule.ScheduleListViewModel;
import com.example.target_club_in_donga.Schedule.ScheduleAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements HomeActivity_Fragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    //private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ScheduleActivity_Adapter fragmentAdapter = new ScheduleActivity_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        // 홈에서 일정을 눌었을 떄, viewPager에서 ViewPagerAdapter_Schedule로 일정화면이 나오고
        // 오른쪽에서 왼쪽으로 슬라이드를 하면 홈 화면이 나오도록 한다.




    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}