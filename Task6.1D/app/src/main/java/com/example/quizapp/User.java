package com.example.quizapp;

import java.util.List;

public class User {
    private String id;
    private String fullName;
    private String username;
    private List<Task> taskList;
    private String avatarUrl;

    private List<String> interestList;
    public User() {
    }

    public List<String> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<String> interestList) {
        this.interestList = interestList;
    }

    public User(String id, String fullName, String username, List<Task> taskList, String avatarUrl, List<String> interestList) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.taskList = taskList;
        this.avatarUrl = avatarUrl;
        this.interestList = interestList;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }



    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public List<Task> getTaskList() {
        return taskList;
    }


}
