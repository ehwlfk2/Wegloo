package com.example.target_club_in_donga.calendar.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Data Access Object
@Dao
public interface TodoDao {

    @Query("SELECT * FROM Todo")
        //관찰하려는 객체를 LiveData<T> 형태로 감싸줍니다.
    LiveData<List<Todo>> getAll();

    @Query("SELECT * FROM Todo WHERE id IN (:index)")
    List<Todo> loadAllByIds(int[] index);

    @Query("SELECT * FROM Todo WHERE timeStamp IN (:time)")
    LiveData<List<Todo>> loadAllByTimeStamp(long time);

    @Query("SELECT * FROM Todo WHERE title LIKE :context")
    Todo findByName(String context);

    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);
}
