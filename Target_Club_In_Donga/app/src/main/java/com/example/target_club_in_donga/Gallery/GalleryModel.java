package com.example.target_club_in_donga.Gallery;

import java.io.Serializable;
import java.util.ArrayList;
import com.example.target_club_in_donga.Gallery.*;

public class GalleryModel implements Serializable {
    public String title;
    public String contents;
    public String username;
    public String uid;
    public Object timestamp;
    public int idx = 0;
    public ArrayList<String> imglist = new ArrayList<>();
}
