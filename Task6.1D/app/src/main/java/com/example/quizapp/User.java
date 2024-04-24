package com.example.quizapp;

import java.util.List;

public class UserData {
    private String fullName;
    private String userName;
    private List<Task> taskList;
    private String avatar;
    public UserData() {
    }

    public UserData(String fullName, String userName, List<Task> taskList, String avatar) {
        this.fullName = fullName;
        this.userName = userName;
        this.taskList = taskList;
        this.avatar = avatar;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public String getAvatar() {
        return avatar;
    }
}
