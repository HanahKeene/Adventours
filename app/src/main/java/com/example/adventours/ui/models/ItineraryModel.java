// ItineraryModel.java
package com.example.adventours.ui.models;

public class ItineraryModel {

    String id;

    private String name;
    private String image;

    public ItineraryModel() {
        // Required empty public constructor for Firestore
    }

    public ItineraryModel(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
