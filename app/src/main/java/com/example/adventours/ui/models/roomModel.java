package com.example.adventours.ui.models;

public class roomModel {
    private String img_url;
    private String name;
    private String price;
    private String room_id;



    public roomModel(String room_id, String img_url, String name, String price) {
        this.img_url = img_url;
        this.name = name;
        this.price = price;
        this.room_id = room_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
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
