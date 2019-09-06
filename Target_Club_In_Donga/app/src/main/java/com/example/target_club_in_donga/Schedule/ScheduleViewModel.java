package com.example.target_club_in_donga.Schedule;

import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class ScheduleViewModel extends ViewModel {
    private TSLiveData<Calendar> mSchedule = new TSLiveData<>();

    public void setSchedule(Calendar calendar) {this.mSchedule.setValue(calendar); }
}
