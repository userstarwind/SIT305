package com.example.task71p;

public class Advert {
    int id;
    String type;
    String name;
    String description;
    String phone;
    String date;
    String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Advert(int id, String type, String name, String description, String phone, String date, String location) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.date = date;
        this.location = location;
    }
}
