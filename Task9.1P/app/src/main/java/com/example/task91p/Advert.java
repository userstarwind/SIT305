package com.example.task91p;

public class Advert {
    int id;
    String type;
    String name;
    String description;
    String phone;
    String date;
    String placeName;
    String placeAddress;
    Double placeLatitude;
    Double placeLongitude;

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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Advert(int id, String type, String name, String description, String phone, String date, String placeName, String placeAddress, Double placeLatitude, Double placeLongitude) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.date = date;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
    }
}
