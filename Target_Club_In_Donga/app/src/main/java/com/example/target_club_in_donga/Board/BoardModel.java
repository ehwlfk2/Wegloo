package com.example.target_club_in_donga.Board;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardModel implements Serializable {
    public String title;
    public String contents;
    public String username;
    public String uid;
    public Object timestamp;
    public int idx = 0;
    public ArrayList<String> imglist = new ArrayList<>();
    public ArrayList<String> imgName = new ArrayList<>();
}
