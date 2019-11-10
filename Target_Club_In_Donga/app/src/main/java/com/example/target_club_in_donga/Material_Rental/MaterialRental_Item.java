package com.example.target_club_in_donga.Material_Rental;

public class MaterialRental_Item {
    private String id, text;
    public Object timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String imageUri;
    public String imageName;
    public String title;
    public String lender;
    public int state;

    public MaterialRental_Item(){

    }

    public Object getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(Object timestamp){
        this.timestamp = timestamp;
    }
    public String getText() {return text; }

}