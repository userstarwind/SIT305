package com.example.intellicareer;

import android.app.Application;

import com.example.intellicareer.ui.home.News;
import com.google.firebase.auth.FirebaseUser;

public class MyApplication extends Application {
    private DBHelper dbHelper;
    private FirebaseUser currentUser;
    private News selectedNews;
    private Job SelectedJob;
    private String CVContent;
    private String CVEvaluation;
    private Job recruitJob;

    public Job getRecruitJob() {
        return recruitJob;
    }

    public void setRecruitJob(Job recruitJob) {
        this.recruitJob = recruitJob;
    }
    public String getCVEvaluation() {
        return CVEvaluation;
    }

    public void setCVEvaluation(String CVEvaluation) {
        this.CVEvaluation = CVEvaluation;
    }

    public String getCVContent() {
        return CVContent;
    }

    public void setCVContent(String CVContent) {
        this.CVContent = CVContent;
    }

    public Job getSelectedJob() {
        return SelectedJob;
    }

    public void setSelectedJob(Job selectedJob) {
        SelectedJob = selectedJob;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setSelectedNews(News selectedNews) {
        this.selectedNews = selectedNews;
    }

    public News getSelectedNews() {
        return selectedNews;
    }

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
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
