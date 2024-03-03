package com.example.adventours.ui.models;

import java.util.List;

public class individualitineraryModel {

    String id;

    String date;

    private List<individualitineraryactivityModel> activityModels;

    public individualitineraryModel(){

    }

    public individualitineraryModel(String id, String date, List<individualitineraryactivityModel> activityModels) {
        this.id = id;
        this.date = date;
        this.activityModels = activityModels;
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

    public List<individualitineraryactivityModel> getActivityModels() {
        return activityModels;
    }

    public void setActivityModels(List<individualitineraryactivityModel> activityModels) {
        this.activityModels = activityModels;
    }
}
