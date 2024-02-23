package com.example.adventours.ui.models;

public class eventModel {

    String eventid;
    String name;
    String img_url;
    String desc;


    public eventModel(String img_url, String name, String desc) {
        this.eventid = eventid;
        this.name = name;
        this.img_url = img_url;
        this.desc = desc;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
