package com.example.adventours.ui.models;

public class HotelListModel {

    private String img_url;
    private String name;

    private String hotel_id;

    public HotelListModel() {
    }

    public HotelListModel(String img_url, String name, String hotel_id) {
        this.img_url = img_url;
        this.name = name;
        this.hotel_id = hotel_id;
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
}
