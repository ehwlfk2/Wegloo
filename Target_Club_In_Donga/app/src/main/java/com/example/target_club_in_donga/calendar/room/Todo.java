package com.example.target_club_in_donga.calendar.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Room 에서 사용하는 Entity 가 되기위해서 선언
@Entity
public class Todo {

    // 자동으로 PrimaryKey 를 설정한다.
    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean isChecked;
    private String title;
    private long timeStamp;

    // Constructor
    public Todo(String title, boolean isChecked, long timeStamp) { this.title = title; this.isChecked = isChecked; this.timeStamp = timeStamp; }

    // 내용을 확인할 수 있도록 재정의
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isChecked=" + isChecked +
                "}" + '\n';
    }

    // ID getter, setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // isCehcked getter, setter
    public boolean getIsChecked() { return isChecked; }
    public void setIsChecked(boolean isChecked) { this.isChecked = isChecked; }

    // title getter, setter
    public String getTitle() {return title; }
    public void setTitle(String title) { this.title = title; }

    // timeStamp getter, setter
    public long getTimeStamp() { return timeStamp; }
    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }
}