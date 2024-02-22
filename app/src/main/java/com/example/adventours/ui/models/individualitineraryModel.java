package com.example.adventours.ui.models;

import java.util.Date;
import java.util.List;

public class individualitineraryModel {

    String id;

    String date;

    private List<String> activities;

    public individualitineraryModel(){

    }

    public individualitineraryModel(String id, String date, List<String> activities) {
        this.id = id;
        this.date = date;
        this.activities = activities;
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

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }
}
