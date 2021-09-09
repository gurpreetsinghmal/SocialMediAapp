package com.technominds.lecture.socialmediaapp.CustomModels;

public class PostModel {
    String title,desc,post_image,owner,key,uid;

    public PostModel() {
    }

    public PostModel(String title, String desc, String post_image, String owner, String key, String uid) {
        this.title = title;
        this.desc = desc;
        this.post_image = post_image;
        this.owner = owner;
        this.key = key;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
