package com.example.target_club_in_donga.calendar.room;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RefreshKeyDao {
    @Query("SELECT * FROM RefreshKey")
    List<RefreshKey> getAll();

    @Query("SELECT refreshKey FROM RefreshKey WHERE clubName LIKE :clubName")
    int findByIdString(String clubName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RefreshKey refreshKey);

    @Update
    void update(RefreshKey refreshKey);

    @Delete
    void delete(RefreshKey refreshKey);
}
