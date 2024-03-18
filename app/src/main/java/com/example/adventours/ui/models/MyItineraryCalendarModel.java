package com.example.adventours.ui.models;

public class MyItineraryCalendarModel {

    String date;
    String activity;

    public MyItineraryCalendarModel(String date, String activity) {
        this.date = date;
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
