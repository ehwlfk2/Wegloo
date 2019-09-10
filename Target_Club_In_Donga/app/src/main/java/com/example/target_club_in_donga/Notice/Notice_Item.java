package com.example.target_club_in_donga.Notice;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notice_Item {
    private String title;
    private String content;
    private boolean switchOnOff;
    private Object timestamp;
    private String writer;
    public List<Notice_item_color> notice_item_colors = new ArrayList<>();
    /*public Notice_Item(String writer,String title, String content, boolean switchOnOff, Object timestamp) {
        this.title = title;
        this.content = content;
        this.switchOnOff = switchOnOff;
        this.timestamp = timestamp;
        this.writer = writer;
    }*/


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSwitchOnOff() {
        return switchOnOff;
    }

    public void setSwitchOnOff(boolean switchOnOff) {
        this.switchOnOff = switchOnOff;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

}
