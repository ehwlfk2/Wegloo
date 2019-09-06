package com.example.target_club_in_donga.Schedule;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.target_club_in_donga.Schedule.DateFormat.*;

public class ScheduleListViewModel extends ViewModel {
    private static final String TAG = "develop_check";
    private long mCurrentTime;

    public TSLiveData<String> mTitle = new TSLiveData<>();
    public TSLiveData<ArrayList<Object>> mScheduleList = new TSLiveData<>(new ArrayList<Object>());

    public int mCenterPosition;

    public void setTitle(int position) {
        try {
            Object item = mScheduleList.getValue().get(position);
            if (item instanceof Long) {
                setTitle((Long) item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "setTitle 에서 발생! => " + e);
        }
    }

    private void setTitle(long time) {
        mCurrentTime = time;
        mTitle.setValue(DateFormat.getDate(time, CALENDAR_HEADER_FORMAT));
    }

    public void initScheduleList() {
        GregorianCalendar cal = new GregorianCalendar();
        setScheduleList(cal);
    }

    private void setScheduleList(GregorianCalendar cal) {
        setTitle(cal.getTimeInMillis());

        ArrayList<Object> scheduleList = new ArrayList<>();

        for (int i = -300; i < 300; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                if (i == 0) {
                    mCenterPosition = scheduleList.size();
                }
                scheduleList.add(calendar.getTimeInMillis());

                // 해당 월에 시작하는요일 -1 을 하면 빈칸을 구할 수 있다.
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                // 해당 월에 마지막 요일
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                for(int j = 0; j < dayOfWeek; j++){
                    // scheduleList.add(Keys.EMPTY);
                    scheduleList.add(new GregorianCalendar(calendar.get(Calendar.YEAR - 1), calendar.get(Calendar.MONTH), j));
                }
                for(int j = 1; j<= max; j++){
                    scheduleList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.v(TAG, "setTitle 에서 발생! => " + e);
            }
            mScheduleList.setValue(scheduleList);
        }   // for


    }   // setScheduleList
}
