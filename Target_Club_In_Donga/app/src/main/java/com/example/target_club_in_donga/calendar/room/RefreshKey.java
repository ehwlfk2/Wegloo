package com.example.target_club_in_donga.calendar.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public
class RefreshKey {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int refreshKey;
    private String clubName;

    // 생성자
    public RefreshKey(int refreshKey, String clubName) {
        this.refreshKey = refreshKey;
        this.clubName = clubName;
    }

    // id getter, setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // getClubName getter, setter
    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }

    // refreshKey getter, setter
    public int getRefreshKey() {
        return refreshKey;
    }
    public void setRefreshKey(int refreshKey) {
        this.refreshKey = refreshKey;
    }
}
