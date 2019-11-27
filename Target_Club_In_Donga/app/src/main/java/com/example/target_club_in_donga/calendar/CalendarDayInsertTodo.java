package com.example.target_club_in_donga.calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarInsertViewModel;
import com.example.target_club_in_donga.calendar.utils.DateFormat;
import com.example.target_club_in_donga.databinding.ActivityCalendarDayInsertTodoBinding;

import static com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarInsertViewModel.mCurrentDataTime;

import java.util.Calendar;


public class CalendarDayInsertTodo extends AppCompatActivity {

    ActivityCalendarDayInsertTodoBinding binding;
    CalendarInsertViewModel model;
    Intent intent;

    String title;
    boolean bool;

    int calYear, calMonth, calDay, id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_day_insert_todo);
        model = ViewModelProviders.of(this).get(CalendarInsertViewModel.class);
        binding.setModel(model);
        binding.setLifecycleOwner(this);


        binding.activityCalendarDayInsertTooInsertTodoFab.setOnClickListener(v -> {
            mCurrentDataTime = DateFormat.getTimeFromString(calYear, calMonth, calDay);
            model.insert(model.getNewTodo(), model.getWorkIsChecked(), mCurrentDataTime/86400000, true);
            // viewModel 에 저장해둘까..?
            // intent.putExtra("alertIsChecked", binding.activityCalendarDayInsertTodoPushAlertSwitch.isChecked());
            // intent.putExtra("toDoIsChecked", binding.activityCalendarDayInsertTodoWorkCheckBox.isChecked());
            // intent.putExtra("toDoTitleString", binding.activityCalendarDayInsertTodoWorkEditText.getText());
            startActivity(intent);
            finish();
        });

        binding.activityCalendarDayInsertTooDeleteTodoFab.setOnClickListener(v -> {
            try {
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.v("develop_Log_v", "activityCalendarDayInsertTooDeleteTodoFab Error: " + e);
                e.printStackTrace();
            }
        });

        binding.activityCalendarDayInsertTodoDatePickerTextView.setOnClickListener(v -> Dialog_DatePicker());

        // 초기 설정
        if (model != null) {
            intent = getIntent();
            id = intent.getIntExtra("DB_id", -1);
            if (id != -1) {
                int[] index = {id};
                model.delete(index);
            }
            try {
                bool = intent.getBooleanExtra("checked", false);
                title = intent.getStringExtra("title");
            } catch (Exception e) {
                title = "";
                bool = false;
            } finally {
                model.setNewTodo(title);
                model.setWorkIsChecked(bool);
            }

            binding.activityCalendarDayInsertTodoDatePickerTextView.setText(
                    model.initTime(getApplicationContext()));

            Calendar cal = Calendar.getInstance();
            calYear = cal.get(Calendar.YEAR);
            calMonth = cal.get(Calendar.MONTH);
            calDay = cal.get(Calendar.DAY_OF_MONTH);
            intent = new Intent(CalendarDayInsertTodo.this, CalendarDay.class);
        }
    }

    private void Dialog_DatePicker() {
        StringBuilder currentTime = new StringBuilder();
        DatePickerDialog.OnDateSetListener callbackMethod = (view, year, month, dayOfMonth) -> {
            calYear = year;
            calMonth = month;
            calDay = dayOfMonth;
            currentTime.append(year).append("년 ").append(month + 1).append("월 ").append(dayOfMonth).append("일");
            binding.activityCalendarDayInsertTodoDatePickerTextView.setText(currentTime.toString());
        };
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, calYear, calMonth, calDay);
        dialog.show();
    }
}

    /*
    private void Dialog_TimePicker(){
        TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {

                if(hour < 10)
                    timeStr = "0"+hour;
                else
                    timeStr = hour+"";

                if(min < 10)
                    timeStr += ":0"+min;
                else
                    timeStr += ":"+min;

                activityvote_insert_textview_time.setText(timeStr);
            }
        };
        Calendar t = Calendar.getInstance();
        int thour = t.get(Calendar.HOUR_OF_DAY);
        int tmin = t.get(Calendar.MINUTE);
        TimePickerDialog talert = new TimePickerDialog(this, mTimeSetListener,thour, tmin,false);
        talert.show();

        //return timeStr;
    }
     */
