package com.example.target_club_in_donga.calendar.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Room condition 1, 2.
@Database(entities = {RefreshKey.class}, version = 1)
public abstract class CalendarRefreshDatabase extends RoomDatabase {

    // data access object
    public abstract RefreshKeyDao refreshKeyDao();
}