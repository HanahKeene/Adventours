package com.example.adventours.ui.models;

public class roomModel {

    private String amenities;
    private String desc;
    private String img_url;
    private String name;
    private String price;

    public roomModel() {
    }

    public roomModel(String amenities, String desc, String img_url, String name, String price) {
        this.amenities = amenities;
        this.desc = desc;
        this.img_url = img_url;
        this.name = name;
        this.price = price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
