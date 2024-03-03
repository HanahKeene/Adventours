package com.example.adventours.ui.models;

public class individualitineraryactivityModel {

    String name;
    String place;
    String documentId;
    String dayId;
    String itineraryId;

    public individualitineraryactivityModel(){

    }

    public individualitineraryactivityModel(String name, String place, String documentId, String dayId, String itineraryId) {
        this.name = name;
        this.place = place;
        this.documentId = documentId;
        this.dayId = dayId;
        this.itineraryId = itineraryId;
    }

    public individualitineraryactivityModel(String name, String place, String documentId, String dayId) {
        this.name = name;
        this.place = place;
        this.documentId = documentId;
        this.dayId = dayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId) {
        this.itineraryId = itineraryId;
    }
}
