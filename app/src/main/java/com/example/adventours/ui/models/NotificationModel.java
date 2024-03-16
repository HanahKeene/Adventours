package com.example.adventours.ui.models;

import java.security.Timestamp;

public class NotificationModel {

    String description;
    String title;
    String status;
    String reservation_id;
    String reservation_category;
    String document_id;

    public NotificationModel(){


    }

    public NotificationModel(String description, String title, String status, String reservation_id, String reservation_category, String document_id) {
        this.description = description;
        this.title = title;
        this.status = status;
        this.reservation_id = reservation_id;
        this.reservation_category = reservation_category;
        this.document_id = document_id;

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

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getReservation_category() {
        return reservation_category;
    }

    public void setReservation_category(String reservation_category) {
        this.reservation_category = reservation_category;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }
}
