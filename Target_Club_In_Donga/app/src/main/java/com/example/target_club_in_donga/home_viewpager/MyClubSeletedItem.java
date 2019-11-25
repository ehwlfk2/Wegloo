package com.example.target_club_in_donga.home_viewpager;

import android.graphics.Bitmap;

public class MyClubSeletedItem {
    private String signUpclubName;
    private String signUpclubUid;
    private String signUpclubProfile;
    private boolean signUpclubRealName;
    private boolean approvalCompleted;
    //public Bitmap signUpclubProfileBitmap;

    public String getSignUpclubName() {
        return signUpclubName;
    }

    public void setSignUpclubName(String signUpclubName) {
        this.signUpclubName = signUpclubName;
    }

    public String getSignUpclubUid() {
        return signUpclubUid;
    }

    public void setSignUpclubUid(String signUpclubUid) {
        this.signUpclubUid = signUpclubUid;
    }

    public String getSignUpclubProfile() {
        return signUpclubProfile;
    }

    public void setSignUpclubProfile(String signUpclubProfile) {
        this.signUpclubProfile = signUpclubProfile;
    }

    public boolean isApprovalCompleted() {
        return approvalCompleted;
    }

    public void setApprovalCompleted(boolean approvalCompleted) {
        this.approvalCompleted = approvalCompleted;
    }

    public boolean isSignUpclubRealName() {
        return signUpclubRealName;
    }

    public void setSignUpclubRealName(boolean signUpclubRealName) {
        this.signUpclubRealName = signUpclubRealName;
    }

}
