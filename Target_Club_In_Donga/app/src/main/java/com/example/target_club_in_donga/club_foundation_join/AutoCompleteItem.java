package com.example.target_club_in_donga.club_foundation_join;

import android.graphics.Bitmap;

public class AutoCompleteItem {
    public String title ;
    public Bitmap image = null;
    public String imageUrl ;
    public String clubUid ;
    public String toString() {
        return this.title.toString();
    }
}
