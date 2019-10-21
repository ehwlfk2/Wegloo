package com.example.target_club_in_donga.Package_LogIn;

import java.util.HashMap;
import java.util.Map;

public class AppLoginData {
    private String name;
    private String phone;
    private String recentClub;
    //private String studentNumber;
    //private String school;
    public Map<String, Object> signUpClub  = new HashMap<>();

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

}
