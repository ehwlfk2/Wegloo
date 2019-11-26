package com.example.target_club_in_donga.calendar.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimeLineDao {

    @Query("SELECT * FROM TimeLine")
    LiveData<List<TimeLine>> getAll();

}
