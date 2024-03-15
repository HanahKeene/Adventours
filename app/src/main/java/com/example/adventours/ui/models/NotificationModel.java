package com.example.adventours.ui.models;

import java.security.Timestamp;

public class NotificationModel {

    String description;
    String title;
    String status;

    public NotificationModel(){


    }

    public NotificationModel(String description, String title, String status) {
        this.description = description;
        this.title = title;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
