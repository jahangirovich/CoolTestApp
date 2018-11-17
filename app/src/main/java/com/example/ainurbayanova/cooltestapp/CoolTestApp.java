package com.example.ainurbayanova.cooltestapp;

import android.app.Application;

import com.firebase.client.Firebase;

public class CoolTestApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
