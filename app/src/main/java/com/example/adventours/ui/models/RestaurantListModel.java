package com.example.adventours.ui.models;

public class RestaurantListModel {

    private String img_url;
    private String name;

    private String restau_id;

    public RestaurantListModel() {
    }

    public RestaurantListModel(String img_url, String name, String restau_id) {
        this.img_url = img_url;
        this.name = name;
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

    public String getRestau_id() {
        return restau_id;
    }

    public void setRestau_id(String restau_id) {
        this.restau_id = restau_id;
    }
}
