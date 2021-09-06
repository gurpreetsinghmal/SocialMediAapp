package com.technominds.lecture.socialmediaapp.CustomModels;

public class IntroModel {
    int intro_image;
    String intro_title;

    public IntroModel(int intro_image, String intro_title) {
        this.intro_image = intro_image;
        this.intro_title = intro_title;
    }

    public int getIntro_image() {
        return intro_image;
    }

    public void setIntro_image(int intro_image) {
        this.intro_image = intro_image;
    }

    public String getIntro_title() {
        return intro_title;
    }

    public void setIntro_title(String intro_title) {
        this.intro_title = intro_title;
    }
}
