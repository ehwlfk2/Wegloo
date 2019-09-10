package com.example.target_club_in_donga.Package_LogIn;

public class LoginData {
    private String name;



    private String phone;
    private String studentNumber;
    private String school;
    private int admin;
    private boolean pushAlarmOnOff;

    public LoginData(String name, String phone, String studentNumber, String school, int admin, boolean pushAlarmOnOff) {
        this.name = name;
        this.phone = phone;
        this.studentNumber = studentNumber;
        this.school = school;
        this.admin = admin;
        this.pushAlarmOnOff = pushAlarmOnOff;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
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

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public boolean isPushAlarmOnOff() {
        return pushAlarmOnOff;
    }

    public void setPushAlarmOnOff(boolean pushAlarmOnOff) {
        this.pushAlarmOnOff = pushAlarmOnOff;
    }


}
