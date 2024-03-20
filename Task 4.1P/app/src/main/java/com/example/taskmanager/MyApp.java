package com.example.taskmanager;

import android.app.Application;
import android.widget.Button;

import com.example.taskmanager.database.DatabaseHelper;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
    }


}

