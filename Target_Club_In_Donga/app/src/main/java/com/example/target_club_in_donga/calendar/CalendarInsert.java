package com.example.target_club_in_donga.calendar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarInsertViewModel;
import com.example.target_club_in_donga.databinding.InsertCalendarBinding;

public class CalendarInsert extends AppCompatActivity {

    InsertCalendarBinding binding;
    CalendarInsertViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.insert_calendar);
        model = ViewModelProviders.of(this).get(CalendarInsertViewModel.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

    }
}
