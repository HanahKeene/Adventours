package com.example.adventours.ui.models;

public class individualitineraryactivityModel {

    String name;
    String place;
    String documentId;

    public individualitineraryactivityModel(){

    }

    public individualitineraryactivityModel(String name, String place, String documentId) {
        this.name = name;
        this.place = place;
        this.documentId = documentId;
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
}
