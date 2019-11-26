package com.example.target_club_in_donga.calendar.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.target_club_in_donga.calendar.data.TSLiveData;
import com.example.target_club_in_donga.calendar.utils.DateFormat;
import com.example.target_club_in_donga.calendar.utils.Keys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// class Keys -> public static final String EMPTY = "empty";

public class CalendarListViewModel extends ViewModel {
    public static Long mCurrentTime;
    public static String mCurrentMonth;
    final private int mBegin_Month = -2, mEnd_Month = 2;

    public TSLiveData<String> mTitle = new TSLiveData<>();
    public TSLiveData<ArrayList<Object>> mCalendarList = new TSLiveData<>(new ArrayList<>());

    public int mCenterPosition;

    public void setTitle(long time) {
        mCurrentTime = time/86400000;
        mTitle.setValue(DateFormat.getDate(time, DateFormat.CALENDAR_HEADER_FORMAT));
    }

    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    public void setCalendarList(GregorianCalendar cal) {

        // Title
        setTitle(cal.getTimeInMillis());

        ArrayList<Object> calendarList = new ArrayList<>();

        for (int i = mBegin_Month; i < mEnd_Month; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                if (i == 0) {
                    mCenterPosition = calendarList.size();
                }
                calendarList.add(calendar.getTimeInMillis());

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                for (int j = 0; j < 7; j++) {
                    calendarList.add(Keys.DayOfWeek[j]);
                }
                for (int j = 0; j < dayOfWeek; j++) {   // Empty
                    //calendarList.add(Keys.EMPTY);
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), -dayOfWeek + j + 1));
                }
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                }
                int addEmptyDay = 42 - (max + dayOfWeek);
                for (int j = 0; j < addEmptyDay; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, j + 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCalendarList.setValue(calendarList);
    }//setCalendarList
}