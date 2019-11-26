package com.example.target_club_in_donga.calendar.ui.viewmodel;

import com.example.target_club_in_donga.calendar.data.TSLiveData;

import androidx.lifecycle.ViewModel;

public class CalendarHeaderViewModel extends ViewModel {
    public TSLiveData<Long> mHeaderDate = new TSLiveData<>();

    public void setHeaderDate(long headerDate) {
        this.mHeaderDate.setValue(headerDate);
    }
}
