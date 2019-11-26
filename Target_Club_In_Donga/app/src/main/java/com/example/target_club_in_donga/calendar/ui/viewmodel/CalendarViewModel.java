package com.example.target_club_in_donga.calendar.ui.viewmodel;

import android.util.Log;

import com.example.target_club_in_donga.calendar.data.TSLiveData;

import java.util.Calendar;

import androidx.lifecycle.ViewModel;

public class CalendarViewModel extends ViewModel {
    public TSLiveData<Calendar> mCalendar = new TSLiveData<>();
    private TSLiveData<Boolean> mEvent = new TSLiveData<>();
    private TSLiveData<Boolean> mClickCheck = new TSLiveData<>();


    // mCalendar
    public void setCalendar(Calendar calendar) {
        this.mCalendar.setValue(calendar);
    }
    public Calendar getCalendar() {
        return this.mCalendar.getValue();
    }

    // clickCheckConfirm
    public void setClickCheck(Boolean bool) {
        this.mClickCheck.setValue(bool);
    }

    public boolean getClickCheck() {
        try {
            // warning to NULLPointerException
            return this.mClickCheck.getValue();
        } catch (NullPointerException e) {
            // return to ignored
            Log.v("develop_Log_v", "NullPointerException in getClickCheck: " + e);
            return false;
        }
    }

    public Long getTimeStamp(){
        return this.mCalendar.getValue().getTimeInMillis();
    }

    // eventConfirm
    public void setEvent(Boolean bool) {
        this.mEvent.setValue(bool);
    }

    public boolean getEvent() {
        try {
            // warning to NULLPointerException
            return this.mEvent.getValue();
        } catch (NullPointerException e) {
            // return to ignored
            Log.v("develop_Log_v", "NullPointerException in getEvent: " + e);
            return false;
        }
    }

}
