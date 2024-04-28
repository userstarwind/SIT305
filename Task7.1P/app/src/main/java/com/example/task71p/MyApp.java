package com.example.task71p;

import android.app.Application;

public class MyApp extends Application {
    private DBHelper db;

    public DBHelper getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = DBHelper.getInstance(this);
    }


}
