package com.example.adventours.ui.models;

public class searchModel {
    private String id;
    private String name;
    private String spot_id;
    private String hotel_id;
    private String restau_id;

    searchModel(){

    }

    public searchModel(String id, String name, String spot_id, String hotel_id, String restau_id) {
        this.id = id;
        this.name = name;
        this.spot_id = spot_id;
        this.hotel_id = hotel_id;
        this.restau_id = restau_id;
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

    public String getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(String spot_id) {
        this.spot_id = spot_id;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getRestau_id() {
        return restau_id;
    }

    public void setRestau_id(String restau_id) {
        this.restau_id = restau_id;
    }
}