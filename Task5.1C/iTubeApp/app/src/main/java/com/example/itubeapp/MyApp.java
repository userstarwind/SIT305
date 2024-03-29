package com.example.itubeapp;

import android.app.Application;

import com.example.itubeapp.database.DatabaseHelper;

public class MyApp extends Application {
    private int UID;

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
    }
}
