package com.example.target_club_in_donga.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.target_club_in_donga.R;

public class ScheduleActivity_Fragment extends Fragment {

    public ScheduleActivity_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acitivity_schedule, container, false);
        return  view;
    } // activity_schedule에 있는 화면을 가지고 온다

}