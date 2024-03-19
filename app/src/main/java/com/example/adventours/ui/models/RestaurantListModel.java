package com.example.adventours.ui.models;

public class RestaurantListModel {

    private String img_url;
    private String name;
    private double adult_fee;
    private double child_fee;
    private String restau_id;

    public RestaurantListModel() {
    }

    public RestaurantListModel(String img_url, String name, double adult_fee, double child_fee, String restau_id) {
        this.img_url = img_url;
        this.name = name;
        this.adult_fee = adult_fee;
        this.child_fee = child_fee;
        this.restau_id = restau_id;
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

    public double getAdult_fee() {
        return adult_fee;
    }

    public void setAdult_fee(double adult_fee) {
        this.adult_fee = adult_fee;
    }

    public double getChild_fee() {
        return child_fee;
    }

    public void setChild_fee(double child_fee) {
        this.child_fee = child_fee;
    }

    public String getRestau_id() {
        return restau_id;
    }

    public void setRestau_id(String restau_id) {
        this.restau_id = restau_id;
    }
}
