package com.example.target_club_in_donga.calendar.ui.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.target_club_in_donga.calendar.data.TSLiveData;
import com.example.target_club_in_donga.calendar.room.CalendarRefreshDatabase;
import com.example.target_club_in_donga.calendar.room.RefreshKey;
import com.example.target_club_in_donga.calendar.room.RefreshKeyDao;
import com.example.target_club_in_donga.calendar.utils.DateFormat;
import com.example.target_club_in_donga.calendar.utils.Keys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.target_club_in_donga.MainActivity.clubName;

// class Keys -> public static final String EMPTY = "empty";

public class CalendarListViewModel extends ViewModel {
    public static Long mCurrentTime;
    public static String mCurrentMonth;
    private static int refreshKey;
    final private int mBegin_Month = -2, mEnd_Month = 2;

    CalendarRefreshDatabase db;
    Application application;

    public TSLiveData<String> mTitle = new TSLiveData<>();
    public TSLiveData<ArrayList<Object>> mCalendarList = new TSLiveData<>(new ArrayList<>());

    public int mCenterPosition;

    public void setTitle(long time) {
        mTitle.setValue(DateFormat.getDate(time, DateFormat.CALENDAR_HEADER_FORMAT));
        mCurrentTime = time / 86400000;
    }

    public void initCalendarList(Application application) {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);

        this.application = application;
        db = Room.databaseBuilder(application, CalendarRefreshDatabase.class, "refreshDB")
                /*.allowMainThreadQueries()*/.build();

        getKey(db);
        Log.v("develop_Log_v", "refreshKey: "+ refreshKey);
    }

    private void getKey(CalendarRefreshDatabase db) {
        new findRefreshKeyAsyncTask(db.refreshKeyDao()).execute();
    }

    private static class findRefreshKeyAsyncTask extends AsyncTask<Void, Void, Void> {
        private RefreshKeyDao mRefreshKeyDao;

        findRefreshKeyAsyncTask(RefreshKeyDao mRefreshKeyDao) {  // Tip... Alt + Insert
            this.mRefreshKeyDao = mRefreshKeyDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                refreshKey = mRefreshKeyDao.findKeyByClubName(clubName)[0];
            }
            catch (Exception e){
                Log.v("develop_Log_v", "refreshKey: " + refreshKey);
                RefreshKey singleRefreshKey = new RefreshKey(0,clubName);
                mRefreshKeyDao.insert(singleRefreshKey);
                refreshKey = 0;
            }
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Void, Void, Void> {
        private RefreshKeyDao mRefreshKeyDao;
        int dateNumKey;

        UpdateAsyncTask(RefreshKeyDao mRefreshKeyDao, int dateNumKey) {  // Tip... Alt + Insert
            this.mRefreshKeyDao = mRefreshKeyDao;
            this.dateNumKey = dateNumKey;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RefreshKey SingleRefreshKey = mRefreshKeyDao.loadByClubName(clubName);
            SingleRefreshKey.setRefreshKey(dateNumKey);
            SingleRefreshKey.setRefreshKey(dateNumKey);
            refreshKey = dateNumKey;
            mRefreshKeyDao.update(SingleRefreshKey);
            return null;
        }
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

    public void refreshDB() {
        CalendarInsertViewModel calendarInsertViewModel = new CalendarInsertViewModel(application);
        FirebaseDatabase.getInstance().getReference().child("EveryClub").child(clubName).child("Calendar").child("ToDo").addListenerForSingleValueEvent(new ValueEventListener() {
            int dateNumKey;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("refresh",refreshKey+"");
                //Toast.makeText(application.getApplicationContext(),"RefreshKey:" + refreshKey,Toast.LENGTH_SHORT).show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    List<CalendarDBItem> listFB = new ArrayList<>();    // listFB 하루 데이터 몽땅

                    // refresh action from FB
                    String dateKey = snapshot.getKey();
                    dateNumKey = -1 * Integer.parseInt(dateKey);
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        CalendarDBItem calendarDBItem = snap.getValue(CalendarDBItem.class);
                        listFB.add(calendarDBItem);
                    }   // 데이터 하나

                    // refresh action to DB
                    for (int i = 0; i < listFB.size(); i++) {
                        calendarInsertViewModel.insert(listFB.get(i).title,listFB.get(i).isChecked, listFB.get(i).time, false);
                    }

                    //refreshKey랑 같으면 그날까지만 받고 break refreshKey == 0 이면 아무것도 없기때문에 데이터전부 받아옴
                    if (dateNumKey == refreshKey && refreshKey != 0) {
                        break;
                    }
                }   // 날짜
                new UpdateAsyncTask(db.refreshKeyDao(),dateNumKey).execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}