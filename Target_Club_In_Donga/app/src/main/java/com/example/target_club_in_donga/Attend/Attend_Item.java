package com.example.target_club_in_donga.Attend;

public class Attend_Item {
    private String id;
    public Object timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String clubName;
    public String myAttendState;
    public String startTime;
    public String attendTimeLimit;
    public String tardyTimeLimit;

    public Attend_Item() {
    }

    public Object getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(Object timestamp){
        this.timestamp = timestamp;
    }
}
