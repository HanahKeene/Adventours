package com.example.adventours.ui.models;

public class FYPModel {

    private String img_url;
    private String name;
    private String location;
    private String desc;
    private String spot_id;

    public FYPModel() {
    }

    public FYPModel(String spot_id, String img_url, String name, String location, String desc) {

        this.spot_id = spot_id;
        this.img_url = img_url;
        this.name = name;
        this.location = location;
        this.desc = desc;
    }


    public String getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(String spot_id) {
        this.spot_id = spot_id;
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