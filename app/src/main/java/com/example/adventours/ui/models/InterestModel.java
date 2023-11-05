package com.example.adventours.ui.models;

public class InterestModel {

    String activity;
    String category;

    public InterestModel() {
    }

    public InterestModel(String activity, String category) {
        this.activity= activity;
        this.category = category;
    }


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
