package com.example.adventours.ui.models;

public class RestaurantsModel {

    private String restau_id;
    private String img_url;
    private String name;

    // Add these fields if they exist in your Firestore documents
    private String location;
    private String desc;

    public RestaurantsModel() {
    }


    public RestaurantsModel(String restau_id, String img_url, String name, String location, String desc) {
        this.restau_id = restau_id;
        this.img_url = img_url;
        this.name = name;
        this.location = location;
        this.desc = desc;
    }

    public String getRestau_id() {
        return restau_id;
    }

    public void setRestau_id(String restau_id) {
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


}
