package com.example.adventours.ui.models;

public class ToursListModel {

    private String img_url;
    private String name;
    private String tour_id;
    private String desc;
    private double price;

    public ToursListModel(){

    }

    public ToursListModel(String img_url, String name, String tour_id, String desc, double price) {
        this.img_url = img_url;
        this.name = name;
        this.tour_id = tour_id;
        this.desc = desc;
        this.price = price;
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

    public String getTour_id() {
        return tour_id;
    }

    public void setTour_id(String tour_id) {
        this.tour_id = tour_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
