package com.example.target_club_in_donga.calendar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.ui.adapter.CalendarInsertAdapter;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarInsertViewModel;
import com.example.target_club_in_donga.databinding.ActivityCalendarDayBinding;

public class CalendarDay extends AppCompatActivity implements View.OnClickListener {

    ActivityCalendarDayBinding binding;
    CalendarInsertViewModel model;
    Intent intent;
    long time;
    private Animation fab_open, fab_close, fab_rotate, fab_rotate_close;
    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_day);
        model = ViewModelProviders.of(this).get(CalendarInsertViewModel.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate);
        fab_rotate_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_close);

        // /* testing Code */
        //binding.activityCalendarDayCheckDataTextView.setMovementMethod(new ScrollingMovementMethod());

        // db!
        observe();

        if (model != null) {
            intent = getIntent();
            time = intent.getLongExtra("timestamp", 0);
            binding.activityCalendarDayCheckDataTextView.setText(
                    model.initDB(time));
        }

        binding.activityCalendarDayInsertDayDataFab.setOnClickListener(this);
        binding.activityCalendarDayInsertDayDataFab1.setOnClickListener(this);
        binding.activityCalendarDayInsertDayDataFab2.setOnClickListener(this);
        binding.activityCalendarDayInsertDayDataFab3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.activity_calendar_day_insertDayData_fab:
                anim();
                Toast.makeText(this, "Floating Action Button: fab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_calendar_day_insertDayData_fab1:
                Toast.makeText(this, "Floating Action Button: fab1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_calendar_day_insertDayData_fab2:
                Toast.makeText(this, "Floating Action Button; fab2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_calendar_day_insertDayData_fab3:
                Intent intent = new Intent(CalendarDay.this, CalendarDayInsertTodo.class);
                Toast.makeText(this, "Floating Action Button: fab3", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                break;
        } // switch
    }

    private void anim() {
        if (isFabOpen) { // close
            binding.activityCalendarDayInsertDayDataFab.startAnimation(fab_rotate);
            binding.activityCalendarDayInsertDayDataFab1.startAnimation(fab_close);
            binding.activityCalendarDayInsertDayDataFab2.startAnimation(fab_close);
            binding.activityCalendarDayInsertDayDataFab3.startAnimation(fab_close);
            binding.activityCalendarDayInsertDayDataFab1.setClickable(false);
            binding.activityCalendarDayInsertDayDataFab2.setClickable(false);
            binding.activityCalendarDayInsertDayDataFab3.setClickable(false);
            isFabOpen = false;
        } else { // open
            binding.activityCalendarDayInsertDayDataFab.startAnimation(fab_rotate_close);
            binding.activityCalendarDayInsertDayDataFab1.startAnimation(fab_open);
            binding.activityCalendarDayInsertDayDataFab2.startAnimation(fab_open);
            binding.activityCalendarDayInsertDayDataFab3.startAnimation(fab_open);
            binding.activityCalendarDayInsertDayDataFab1.setClickable(true);
            binding.activityCalendarDayInsertDayDataFab2.setClickable(true);
            binding.activityCalendarDayInsertDayDataFab3.setClickable(true);
            isFabOpen = true;
        }
    }

    private void observe() {
        model.todos.observe(this, objects -> {
            RecyclerView view = binding.dayDataCalendar;
            CalendarInsertAdapter adapter = (CalendarInsertAdapter) view.getAdapter();
            if (adapter != null) {
                adapter.setTodosList(objects);
            } else {
                //LinearLayoutManager
                LinearLayoutManager manager = new LinearLayoutManager(this);
                adapter = new CalendarInsertAdapter(objects, getApplicationContext(), this, time);
                view.setLayoutManager(manager);
                view.setAdapter(adapter);
                if (model.mCenterPosition >= 0) {
                    view.scrollToPosition(model.mCenterPosition);
                }
            }
        });
    } // observe

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /*
    private void observe() {
        /* Testing Code - call function
        model.getCurrentTodo().observe(this, nameObserver);
    }

    /* Testing Code - define function
    final Observer<String> nameObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            binding.insertCalendarMCurrentTextView.setText(s);
        }
    };
     */
}
