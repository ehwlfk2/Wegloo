package com.example.target_club_in_donga.calendar.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//
public class DateFormat {
    public final static String CALENDAR_CONTENT_FORMAT = "a h:mm";
    public final static String DAY_OF_WEEK_FORMAT = "EE";
    public final static String CALENDAR_HEADER_FORMAT = "YYYY년 MM월";
    public final static String DAY_FORMAT = "d";
    public final static String CALENDAR_DAY_FORMAT = "YYYY년 MM월 dd일";

    public static String getDate(long date, String pattern) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
            Date d = new Date(date);
            // toLoweCase() -> 소문자로 바꾸기, toUpperCase -> 대문자로 바꾸기, trim() -> 문자열 좌우 공백 없애기
            return formatter.format(d).toUpperCase();
        } catch (Exception e) {
            Log.v("develop_Log", "getDate Error: " + e);
            return "error";
        }
    }

    public static String getDateHourMinute(long date, String pattern) {
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        }
        catch(Exception e) {
            Log.v("develop_Log","getDate Error: " + e);
            return "error";
        }
    }

    public static String getDayOfWeekFormat(long date, String pattern){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.KOREA);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        }catch (Exception e){
            Log.v("develop_Log","getDayOfWeekFormat Error: " + e);
            return "error";
        }
    }
    public static Long getTimeFromString(int year, int month, int day){
        try{
            GregorianCalendar calendar = new GregorianCalendar(year,month,day,0,0,0);
            return  calendar.getTimeInMillis();
        }catch (Exception e){
            Log.v("develop_Log","getTimeFromString Error: " + e);
            return (long) 0;
        }
    }

}