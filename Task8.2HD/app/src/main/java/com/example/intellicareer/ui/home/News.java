package com.example.intellicareer.ui.home;

public class News {
    private String title;
    private int imageRes;
    private String content;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getContent() {
        return content;
    }

    public News(String title, int imageRes, String content, String date) {
        this.title = title;
        this.imageRes = imageRes;
        this.content = content;
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
