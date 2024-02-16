package com.example.adventours.ui.models.itinerarymodels;

public class ActivityModel {
    private String name;
    private String image; // Assuming you have an image URL

    // Constructor, getters, and setters
    // You can generate them automatically in most IDEs or write them manually
    public ActivityModel() {}

    public ActivityModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

