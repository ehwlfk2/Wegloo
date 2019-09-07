package com.example.target_club_in_donga.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vote_Item {
    public String title;
    //public String date;
    public Object timestamp;
    public ArrayList<Vote_Item_Count> listItems = new ArrayList<Vote_Item_Count>();
    public int totalCount = 0;
    public Map<String, Integer> stars = new HashMap<>();
    //public String uid;
    /*public String getTitle() {
        return title;
    }
    public String getdate() {
        return date;
    }
    public ListItem getListItem() {
        return listItem;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setListItem(ListItem listItem){
        this.listItem = listItem;
    }*/
}
