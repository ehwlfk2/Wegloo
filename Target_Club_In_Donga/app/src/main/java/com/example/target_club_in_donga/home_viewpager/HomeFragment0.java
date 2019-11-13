package com.example.target_club_in_donga.home_viewpager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.target_club_in_donga.R;

import static com.example.target_club_in_donga.home_viewpager.HomeActivityView.viewAdapter;

public class HomeFragment0 extends Fragment implements View.OnClickListener {

    private FrameLayout voteIntentBtn, attendIntentBtn, scheduleIntentBtn, boardIntentBtn;
    /**
     * 홈 화면
     */
    public HomeFragment0() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home0, container, false);
        voteIntentBtn = view.findViewById(R.id.home_frame_vote);
        attendIntentBtn = view.findViewById(R.id.home_frame_attendance);
        scheduleIntentBtn = view.findViewById(R.id.home_frame_calender);
        boardIntentBtn = view.findViewById(R.id.home_frame_board);

        voteIntentBtn.setOnClickListener(this);
        attendIntentBtn.setOnClickListener(this);
        scheduleIntentBtn.setOnClickListener(this);
        boardIntentBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_frame_vote:
                viewAdapter.addItem(new VoteFragment0());
                viewAdapter.notifyDataSetChanged();
                viewAdapter.functionCurrent();
                break;
            case R.id.home_frame_attendance:
                viewAdapter.addItem(new AttendFragment0());
                viewAdapter.notifyDataSetChanged();
                viewAdapter.functionCurrent();
                break;
            case R.id.home_frame_calender:
                viewAdapter.addItem(new ScheduleFragment0());
                viewAdapter.notifyDataSetChanged();
                viewAdapter.functionCurrent();
                break;
            case R.id.home_frame_board:
                viewAdapter.addItem(new BoardFragment0());
                viewAdapter.notifyDataSetChanged();
                viewAdapter.functionCurrent();
                break;
        }
    }
}
