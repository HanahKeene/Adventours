package com.example.adventours.ui.models;

public class HotDealModel {

    String hotdeal_id;

    String img_url;
    String name;
    String type;

    public HotDealModel(String hotdeal_id, String img_url, String name, String type) {
        this.hotdeal_id = hotdeal_id;
        this.img_url = img_url;
        this.name = name;
        this.type = type;
    }

    public HotDealModel() {
    }

    public String getHotdeal_id() {
        return hotdeal_id;
    }

    public void setHotdeal_id(String hotdeal_id) {
        this.hotdeal_id = hotdeal_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
