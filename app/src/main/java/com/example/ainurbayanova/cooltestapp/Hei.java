package com.example.ainurbayanova.cooltestapp;

import java.util.ArrayList;

public class Hei {
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    public Hei(ArrayList<String> usernames){
        this.titles = titles;
        this.usernames = usernames;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(ArrayList<String> usernames) {
        this.usernames = usernames;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }
}
