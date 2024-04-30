package com.example.task91p;

import android.app.Application;

import com.google.android.libraries.places.api.Places;


public class MyApp extends Application {
    private DBHelper db;

    public DBHelper getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = DBHelper.getInstance(this);
        if (!Places.isInitialized()) {
            String apiKey = BuildConfig.MAPS_API_KEY;
            Places.initialize(getApplicationContext(), apiKey);
        }
    }


}
