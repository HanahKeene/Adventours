// ItineraryModel.java
package com.example.adventours.ui.models;

public class ItineraryModel {

    private String name;
    private String imageUrl;

    public ItineraryModel() {
        // Required empty public constructor for Firestore
    }

    public ItineraryModel(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
