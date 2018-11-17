package com.example.ainurbayanova.cooltestapp;

public class tutorialClass {
    private String image,title;

    public tutorialClass(String title, String image){
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
