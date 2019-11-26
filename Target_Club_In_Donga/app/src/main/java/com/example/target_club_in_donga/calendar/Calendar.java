package com.example.target_club_in_donga.calendar;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.ui.adapter.CalendarAdapter;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarListViewModel;
import com.example.target_club_in_donga.databinding.CalendarListBinding;

public class Calendar extends AppCompatActivity {
    private CalendarListBinding binding;
    private CalendarListViewModel model;    // MainActivity - CalendarListViewModel
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        model = ViewModelProviders.of(this).get(CalendarListViewModel.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        observe();
        if (model != null) {
            model.initCalendarList();
        }
    }

    private void observe() {
        model.mCalendarList.observe(this, objects -> {
            RecyclerView view = binding.pagerCalendar;
            CalendarAdapter adapter = (CalendarAdapter) view.getAdapter();
            if (adapter != null) {
                adapter.setCalendarList(objects);
            } else {
                // 격자
                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
                adapter = new CalendarAdapter(objects, getApplicationContext());
                view.setLayoutManager(manager);
                view.setAdapter(adapter);
                if (model.mCenterPosition >= 0) {
                    view.scrollToPosition(model.mCenterPosition);
                }
            }
        });
    }
}