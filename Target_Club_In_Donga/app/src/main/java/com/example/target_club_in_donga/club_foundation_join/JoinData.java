package com.example.target_club_in_donga.club_foundation_join;

public class JoinData {
    private String name;
    private String phone;
    private String realNameProPicDeleteName;
    private String realNameProPicUrl;
    private Object applicationDate;
    private String resume;

    private int admin;
    private boolean pushAlarmOnOff;
    private String pushToken;

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public boolean isPushAlarmOnOff() {
        return pushAlarmOnOff;
    }

    public void setPushAlarmOnOff(boolean pushAlarmOnOff) {
        this.pushAlarmOnOff = pushAlarmOnOff;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getRealNameProPicDeleteName() {
        return realNameProPicDeleteName;
    }

    public void setRealNameProPicDeleteName(String realNameProPicDeleteName) {
        this.realNameProPicDeleteName = realNameProPicDeleteName;
    }

    public String getRealNameProPicUrl() {
        return realNameProPicUrl;
    }

    public void setRealNameProPicUrl(String realNameProPicUrl) {
        this.realNameProPicUrl = realNameProPicUrl;
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

    public Object getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Object applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
