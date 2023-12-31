package com.example.adventours.ui.models;

public class CategoryModel {

    String cat_id;

    String img_url;
    String name;
    String type;

    public CategoryModel() {
    }

    public CategoryModel(String cat_id, String img_url, String name, String type) {
        this.cat_id = cat_id;
        this.img_url = img_url;
        this.name = name;
        this.type = type;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCat_id() {
        return cat_id;
    }
}
