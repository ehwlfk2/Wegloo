package com.example.target_club_in_donga.home_viewpager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.target_club_in_donga.Package_LogIn.LoginActivity;
import com.example.target_club_in_donga.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeActivityView.viewAdapter;

public class HomeFragment0 extends Fragment implements View.OnClickListener {

    private FrameLayout voteIntentBtn, attendIntentBtn, scheduleIntentBtn, boardIntentBtn;
    private TextView home_textview_main;
    private FirebaseDatabase firebaseDatabase;
    /**
     * 홈 화면
     */
    public HomeFragment0() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home0, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        voteIntentBtn = view.findViewById(R.id.home_frame_vote);
        attendIntentBtn = view.findViewById(R.id.home_frame_attendance);
        scheduleIntentBtn = view.findViewById(R.id.home_frame_calender);
        boardIntentBtn = view.findViewById(R.id.home_frame_board);
        home_textview_main = view.findViewById(R.id.home_textview_main);

        firebaseDatabase.getReference().child("EveryClub").child(clubName).child("thisClubName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);
                home_textview_main.setText(title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
