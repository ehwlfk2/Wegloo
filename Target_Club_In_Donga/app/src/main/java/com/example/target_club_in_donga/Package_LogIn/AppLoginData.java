package com.example.target_club_in_donga.Package_LogIn;

import com.example.target_club_in_donga.home_viewpager.MyClubSeletedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppLoginData {
    private String name;
    private String phone;
    private String recentClub;
    private String realNameProPicUrl;
    private String realNameProPicDeleteName;
    private Object joinedTimestamp; //가입날짜
    private String emailLoginEmail;

    public String getRealNameProPicUrl() {
        return realNameProPicUrl;
    }

    public void setRealNameProPicUrl(String realNameProPicUrl) {
        this.realNameProPicUrl = realNameProPicUrl;
    }

    public String getRealNameProPicDeleteName() {
        return realNameProPicDeleteName;
    }

    public void setRealNameProPicDeleteName(String realNameProPicDeleteName) {
        this.realNameProPicDeleteName = realNameProPicDeleteName;
    }
    //public List<MyClubSeletedItem> signUpClub = new ArrayList<>();

    public String getEmailLoginEmail() {
        return emailLoginEmail;
    }

    public void setEmailLoginEmail(String emailLoginEmail) {
        this.emailLoginEmail = emailLoginEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getRecentClub() {
        return recentClub;
    }

    public void setRecentClub(String recentClub) {
        this.recentClub = recentClub;
    }

    public Object getJoinedTimestamp() {
        return joinedTimestamp;
    }

    public void setJoinedTimestamp(Object joinedTimestamp) {
        this.joinedTimestamp = joinedTimestamp;
    }

}
