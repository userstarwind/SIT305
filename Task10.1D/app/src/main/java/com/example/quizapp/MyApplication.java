package com.example.quizapp;

import android.app.Application;

public class MyApplication extends Application {
    private DBHelper dbHelper;
    private User currentUser;
    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = DBHelper.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

