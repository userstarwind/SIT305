package com.example.intellicareer.ui.dashboard;

public class MenuItem {
    private int ImageRes;
    private String text;

    public MenuItem(int imageRes, String text) {
        ImageRes = imageRes;
        this.text = text;
    }

    public int getImageRes() {
        return ImageRes;
    }

    public void setImageRes(int imageRes) {
        ImageRes = imageRes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
