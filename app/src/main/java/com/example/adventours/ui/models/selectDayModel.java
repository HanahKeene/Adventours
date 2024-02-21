package com.example.adventours.ui.models;

public class selectDayModel {

    String Id;

    public selectDayModel() {
        // Required for Firestore deserialization
    }

    public selectDayModel(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
