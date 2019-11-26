package com.example.target_club_in_donga.calendar.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.target_club_in_donga.calendar.data.TSLiveData;


public class EmptyViewModel extends ViewModel {
    public TSLiveData<String> mDayOfWeekData = new TSLiveData<>();

    public void setDayOfWeekData(String DayData) {
        this.mDayOfWeekData.setValue(DayData);
    }
}

