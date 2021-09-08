package com.technominds.lecture.socialmediaapp.CustomModels;

public class UserModel {
    String name,uid,email,image_url;

    public UserModel() {
        //assignment purpose
    }

    public UserModel(String name, String uid, String email, String image_url) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
