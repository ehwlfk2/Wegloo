package com.example.target_club_in_donga.History;

public class History_Item {


    private String imageUrl;
    private String imageDeleteName;
    //private Drawable image ;
    private Object timestamp;
    private String content;
    private boolean selected;
    private boolean selectedYear;

    private boolean visYear;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDeleteName() {
        return imageDeleteName;
    }

    public void setImageDeleteName(String imageDeleteName) {
        this.imageDeleteName = imageDeleteName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public boolean isSelectedYear() {
        return selectedYear;
    }
    public void setSelectedYear(boolean selectedYear) {
        this.selectedYear = selectedYear;
    }

    public boolean isVisYear() {
        return visYear;
    }

    public void setVisYear(boolean visYear) {
        this.visYear = visYear;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
