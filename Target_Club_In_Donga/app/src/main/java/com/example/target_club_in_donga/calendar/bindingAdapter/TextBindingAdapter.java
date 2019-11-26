package com.example.target_club_in_donga.calendar.bindingAdapter;


import android.graphics.Paint;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.utils.DateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarListViewModel.mCurrentMonth;
import static com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarListViewModel.mCurrentTime;

public class TextBindingAdapter {

    @BindingAdapter({"setCalendarHeaderText"})
    public static void setCalendarHeaderText(TextView view, Long date) {
        try {
            if (date != null) {
                mCurrentMonth = DateFormat.getDate(date, DateFormat.CALENDAR_HEADER_FORMAT);
                view.setText(mCurrentMonth);
                //view.setText(DateFormat.getDateHourMinute(date, DateFormat.CALENDAR_CONTENT_FORMAT));
            }
        } catch (Exception e) {
            Log.v("develop_Log_v", "setCalendarHeaderText Error: " + e);
            e.printStackTrace();
        }
    }

    @BindingAdapter({"setDayText"})
    public static void setDayText(TextView view, Calendar calendar) {
        try {
            if (calendar != null) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                view.setText(DateFormat.getDate(gregorianCalendar.getTimeInMillis(), DateFormat.DAY_FORMAT));

                // 해당 달의 day 인지 확인
                if ((DateFormat.getDate(gregorianCalendar.getTimeInMillis(), DateFormat.CALENDAR_HEADER_FORMAT)).equals(mCurrentMonth)) {

                    // 오늘인지 확인
                    if (calendar.getTimeInMillis() / 86400000 == mCurrentTime) {
                        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.calendar_circle));
                        view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorWhite));
                    } else {
                        view.setBackground(null);
                        // 요일 확인
                        String temp = DateFormat.getDate(gregorianCalendar.getTimeInMillis(), DateFormat.DAY_OF_WEEK_FORMAT);
                        if (temp.equals("일")) {
                            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorRed));
                        } else if (temp.equals("토")) {
                            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorBlue));
                        } else {
                            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorBlack));
                        }
                    }
                } else {
                    view.setEnabled(false);
                }
            } // calendar != null
        } catch (
                Exception e) {
            Log.v("develop_Log_v", "setDayText Error: " + e);
            e.printStackTrace();
        }

    }

    @BindingAdapter({"setDayOfWeekText"})
    public static void setDayOfWeekText(TextView view, String string) {
        try {
            view.setText(string);
        } catch (Exception e) {
            Log.v("develop_Log_v", "setDayOfWeekText Error: " + e);
            e.printStackTrace();
        }
    }

    @BindingAdapter({"setTodoTitle"})
    public static void setTodoTitle(TextView view, String string){
        try{
            view.setText(string);
        }catch (Exception e){
            Log.v("develop_Log_v", "setTodoTitle Error: " + e);
            e.printStackTrace();
        }
    }

    @BindingAdapter({"setTodoIsChecked"})
    public static void setTodoIsChecked(CheckBox box, Boolean bool){
        try{
            box.setChecked(bool);
        }catch (Exception e){
            Log.v("develop_Log_v", "setTodoIsChecked Error: " + e);
            e.printStackTrace();
        }
    }
}
