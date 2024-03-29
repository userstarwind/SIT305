package com.example.itubeapp;

public class Song {
    private int SID;
    private int UID;
    private String URL;


    public int getUID() {
        return UID;
    }

    public Song(int SID, int UID, String URL) {
        this.SID = SID;
        this.UID = UID;
        this.URL = URL;
    }

    public int getSID() {
        return SID;
    }

    public String getURL() {
        return URL;
    }
}
