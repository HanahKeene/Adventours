package com.example.adventours.ui.models;

public class HotelsModel {

    private String hotel_id;

    private String img_url;
    private String name;

    // Add these fields if they exist in your Firestore documents
    private String location;
    private String desc;

    public HotelsModel() {
        // Default constructor required for Firestore
    }

    public HotelsModel(String img_url, String name) {
        this.img_url = img_url;
        this.name = name;
        this.name = hotel_id;
    }

    // Getters and setters for existing fields
    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and setters for additional fields
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }
}
