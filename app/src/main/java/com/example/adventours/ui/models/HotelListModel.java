package com.example.adventours.ui.models;

public class HotelListModel {

    private String img_url;
    private String name;

    private String hotel_id;

    private String location;

    private String desc;
    private double lowestPrice;
    private double highestPrice;


    public HotelListModel() {
    }

    public HotelListModel(String img_url, String name, String hotel_id, String location, String desc) {
        this.img_url = img_url;
        this.name = name;
        this.hotel_id = hotel_id;
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

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
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

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }
}
