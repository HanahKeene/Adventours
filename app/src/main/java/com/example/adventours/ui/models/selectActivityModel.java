package com.example.adventours.ui.models;

public class selectActivityModel {

    String name;
    private boolean isChecked;

    selectActivityModel(){

    }

    public selectActivityModel(String name) {
        this.name = name;
        this.isChecked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
