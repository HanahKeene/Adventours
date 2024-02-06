package com.example.adventours.ui.models;

public class ItineraryModel {

    private String id;
    private String name;
    private String image;
    private String start;
    private String end;

    public ItineraryModel(String id, String name, String image, String start, String end) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.start = start;
        this.end = end;
    }

    public ItineraryModel() {
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

    public String getImageUrl() {
        return image;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
