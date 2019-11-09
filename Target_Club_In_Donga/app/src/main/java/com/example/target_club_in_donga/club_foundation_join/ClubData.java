package com.example.target_club_in_donga.club_foundation_join;

public class ClubData {
    private String thisClubName;
    private String clubIntroduce; //소개글
    private String clubImageUrl;
    private String clubImageDeleteName;
    private boolean realNameSystem; //true = 실명제 false = 익명제
    private boolean freeSign; //true = 자유가입제 false = 승인제
    private Object clubCreateTimestamp; //만들어진 일자

    public String getThisClubName() {
        return thisClubName;
    }

    public void setThisClubName(String thisClubName) {
        this.thisClubName = thisClubName;
    }

    public String getClubIntroduce() {
        return clubIntroduce;
    }

    public void setClubIntroduce(String clubIntroduce) {
        this.clubIntroduce = clubIntroduce;
    }

    public String getClubImageUrl() {
        return clubImageUrl;
    }

    public void setClubImageUrl(String clubImageUrl) {
        this.clubImageUrl = clubImageUrl;
    }

    public String getClubImageDeleteName() {
        return clubImageDeleteName;
    }

    public void setClubImageDeleteName(String clubImageDeleteName) {
        this.clubImageDeleteName = clubImageDeleteName;
    }

    public boolean isRealNameSystem() {
        return realNameSystem;
    }

    public void setRealNameSystem(boolean realNameSystem) {
        this.realNameSystem = realNameSystem;
    }

    public boolean isFreeSign() {
        return freeSign;
    }

    public void setFreeSign(boolean freeSign) {
        this.freeSign = freeSign;
    }
    public Object getClubCreateTimestamp() {
        return clubCreateTimestamp;
    }

    public void setClubCreateTimestamp(Object clubCreateTimestamp) {
        this.clubCreateTimestamp = clubCreateTimestamp;
    }
}
