package com.example.adventours.ui.models;

import java.util.ArrayList;

public class TouristSpotDetails {

    private String img_url;
    private String name;
    private String location;
    private String desc;
    private ArrayList<String> imageUrls; // ArrayList to store multiple image URLs

    public TouristSpotDetails() {
        // Default constructor required for Firestore serialization
    }

    public TouristSpotDetails(String img_url, String name, String location, String desc, ArrayList<String> imageUrls) {
        this.img_url = img_url;
        this.name = name;
        this.location = location;
        this.desc = desc;
        this.imageUrls = imageUrls;
    }

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

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
