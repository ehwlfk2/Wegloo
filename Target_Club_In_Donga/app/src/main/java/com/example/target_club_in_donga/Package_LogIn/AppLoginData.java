package com.example.target_club_in_donga.Package_LogIn;

import java.util.HashMap;
import java.util.Map;

public class AppLoginData {
    private String name;
    private String phone;
    private String recentClub;
    private String reailNameProPicUrl;
    private String reailNameProPicDeleteName;

    //public Map<String, Object> signUpClub  = new HashMap<>();

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

    public String getReailNameProPicUrl() {
        return reailNameProPicUrl;
    }

    public void setReailNameProPicUrl(String reailNameProPicUrl) {
        this.reailNameProPicUrl = reailNameProPicUrl;
    }

    public String getReailNameProPicDeleteName() {
        return reailNameProPicDeleteName;
    }

    public void setReailNameProPicDeleteName(String reailNameProPicDeleteName) {
        this.reailNameProPicDeleteName = reailNameProPicDeleteName;
    }

    public String getRecentClub() {
        return recentClub;
    }

    public void setRecentClub(String recentClub) {
        this.recentClub = recentClub;
    }

}
