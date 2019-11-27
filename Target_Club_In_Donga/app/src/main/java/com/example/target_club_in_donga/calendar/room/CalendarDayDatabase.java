package com.example.target_club_in_donga.calendar.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Room condition 1, 2.
@Database(entities = {Todo.class, Schedule.class, TimeLine.class}, version = 1)
public abstract class CalendarDayDatabase extends RoomDatabase {

    // data access object 가 필요하다. -Room condition 3. abstract method... parameter: null, class: @Dao
    public abstract TodoDao todoDao();

    public abstract ScheduleDao scheduledDao();

    public abstract TimeLineDao timeLineDao();
}