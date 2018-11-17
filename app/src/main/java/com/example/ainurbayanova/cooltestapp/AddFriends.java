package com.example.ainurbayanova.cooltestapp;

public class AddFriends {
    private String username, image, key;
    public AddFriends(){

    }
    public AddFriends(String username, String image, String key){
            this.username = username;
            this.image = image;
            this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
