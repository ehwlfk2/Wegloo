package com.example.target_club_in_donga.Notice;

public class Notice_Item {
    private String title;
    private String content;
    private String color;
    private String font;
    private boolean switchOnOff;
    private Object timestamp;

    public Notice_Item(String title, String content, String color, String font, boolean switchOnOff, Object timestamp) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.font = font;
        this.switchOnOff = switchOnOff;
        this.timestamp = timestamp;
    }



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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
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

}
