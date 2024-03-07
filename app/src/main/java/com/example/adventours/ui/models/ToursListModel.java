package com.example.adventours.ui.models;

public class ToursListModel {

    private String img_url;
    private String name;
    private String tour_id;
    private String desc;

    public ToursListModel(){

    }

    public ToursListModel(String img_url, String name, String tour_id, String desc) {
        this.img_url = img_url;
        this.name = name;
        this.tour_id = tour_id;
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
}
