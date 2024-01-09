package com.example.adventours.ui.models;

public class InterestModel {

    String activity;
    String category;

    Boolean clicked;

    public InterestModel() {
    }

    public InterestModel(String activity, String category) {
        this.activity= activity;
        this.category = category;
        this.clicked = false;
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

    public Boolean isClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }
}
