package com.example.target_club_in_donga.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.R;

// 게시판 프래그먼트
/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Notice extends Fragment {


    public Fragment_Notice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notice, container, false);
        return  view;
    } // activity_notice에 있는 화면을 가지고 온다

}
