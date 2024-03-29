package com.example.itubeapp;

public class Video {
    private int VID;
    private int UID;
    private String URL;


    public int getUID() {
        return UID;
    }


    public Video(int VID, int UID, String URL) {
        this.VID = VID;
        this.UID = UID;
        this.URL = URL;
    }

    public int getVID() {
        return VID;
    }

    public String getURL() {
        return URL;
    }
}
