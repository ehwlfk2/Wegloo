package com.example.target_club_in_donga.calendar.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity
class Schedule {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private Long start, end;

    @Ignore
    private String location;

    // id getter, setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // title getter, setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // start getter, setter
    public Long getStart() { return start; }
    public void setStart(Long start) { this.start = start; }

    // end getter, setter
    public Long getEnd() { return end; }
    public void setEnd(Long end) { this.end = end; }

    // location getter, setter
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

}
