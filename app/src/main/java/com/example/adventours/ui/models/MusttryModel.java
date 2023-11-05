package com.example.adventours.ui.models;

public class MusttryModel {

    String img_url;
    String name;

    private String location;
    private String desc;

    public MusttryModel() {
    }

    public MusttryModel(String img_url, String name, String location, String desc) {
        this.img_url = img_url;
        this.name = name;
        this.location = location;
        this.desc = desc;
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
