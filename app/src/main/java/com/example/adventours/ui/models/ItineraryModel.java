package com.example.adventours.ui.models;

public class ItineraryModel {

    private String name;
    private String imageUrl;

    public ItineraryModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public ItineraryModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
