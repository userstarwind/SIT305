package com.example.intellicareer;

public class CustomBean {
    private int imageRes;
    private String describe;

    public CustomBean(int imageRes, String describe) {
        this.imageRes = imageRes;
        this.describe = describe;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
