package com.example.target_club_in_donga;

public class ImageDTO {
    private String id, id2;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String imageUri;
    public String imageName;
    public String edit_name_edittext;
    public String uId;
    public String userId;

    public ImageDTO(String id/*, String id2*/) {
        this.id = id;
//        this.id2 = id2;
    }
    public  ImageDTO(){

    }

}
