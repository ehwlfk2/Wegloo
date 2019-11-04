package com.example.target_club_in_donga.Material_Rental;

public class MaterialRental_History_Item {
    private String id;
    public Object timestamp;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String history_lend_date;
    public String history_lend_name;

    public Object getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(Object timestamp){
        this.timestamp = timestamp;
    }

}
