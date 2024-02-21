package com.example.adventours.ui.models;

public class individualitineraryModel {

    String id;

    String date;

    public individualitineraryModel(){

    }

    public individualitineraryModel(String id, String date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
