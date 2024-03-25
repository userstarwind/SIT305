package com.example.newsapp;

public class News {
    private int id;
    private String title;
    private String content;

    public int getId() {
        return id;
    }

    public News(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
