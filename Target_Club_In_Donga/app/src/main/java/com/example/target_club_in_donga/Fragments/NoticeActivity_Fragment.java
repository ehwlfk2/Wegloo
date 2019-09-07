package com.example.target_club_in_donga.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.Notice.NoticeActivity_Insert;
import com.example.target_club_in_donga.R;
import com.melnykov.fab.FloatingActionButton;

// 게시판 프래그먼트
/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeActivity_Fragment extends Fragment {


    public NoticeActivity_Fragment() {
        // Required empty public constructor
    }


    private FloatingActionButton activity_notice_main_button_intent;
    private RecyclerView activity_notice_main_recyclerview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notice, container, false);

        activity_notice_main_button_intent = (FloatingActionButton)view.findViewById(R.id.activity_notice_main_button_intent);
        activity_notice_main_recyclerview = (RecyclerView)view.findViewById(R.id.activity_notice_main_recyclerview);

        activity_notice_main_button_intent.attachToRecyclerView(activity_notice_main_recyclerview);
        activity_notice_main_button_intent.show();

        activity_notice_main_button_intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity_Insert.class);
                startActivity(intent);
            }
        });
        //요기 구현

        return  view;
    } // activity_notice에 있는 화면을 가지고 온다

}
