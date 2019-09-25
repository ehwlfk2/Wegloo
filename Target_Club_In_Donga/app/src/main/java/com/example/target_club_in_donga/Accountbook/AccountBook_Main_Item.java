package com.example.target_club_in_donga.Accountbook;

public class AccountBook_Main_Item {

    private String priceId;
    private String price;
    private String title;
    private Object timestamp;

    private String imageUrl;
    private String imageDeleteName;

    private boolean selected;

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


    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
