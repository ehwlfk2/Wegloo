package com.example.target_club_in_donga.calendar.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.target_club_in_donga.calendar.data.TSLiveData;


public class CalendarDayToDoViewModel extends ViewModel {
    public TSLiveData<String> mTodoTitleData = new TSLiveData<>();
    public TSLiveData<Boolean> mIsWorkChecked = new TSLiveData<>();

    public void setTodoTitleData(String mTodoTitleData) {
        this.mTodoTitleData.setValue(mTodoTitleData);
    }
    public void setIsWorkChecked(Boolean mIsWorkChecked){
        this.mIsWorkChecked.setValue(mIsWorkChecked);
    }
}
