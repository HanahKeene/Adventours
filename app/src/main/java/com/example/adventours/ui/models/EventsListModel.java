package com.example.adventours.ui.models;

public class EventsListModel {


    String events_id;
    String name;
    String location;
    String start_date;
    String end_date;
    String desc;

    public EventsListModel(String events_id, String name, String location, String start_date, String end_date, String desc) {
        this.events_id = events_id;
        this.name = name;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.desc = desc;
    }

    public String getEvents_id() {
        return events_id;
    }

    public void setEvents_id(String events_id) {
        this.events_id = events_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
