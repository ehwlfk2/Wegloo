package com.example.target_club_in_donga.Material_Management;

public class MaterialManagement_Admin_Item {
    private String id;
    public Object timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String imageUri;
    public String imageName;
    public String edit_name_edittext;
    public String edit_lender;

    public MaterialManagement_Admin_Item(){

    }

    public Object getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(Object timestamp){
        this.timestamp = timestamp;
    }

}