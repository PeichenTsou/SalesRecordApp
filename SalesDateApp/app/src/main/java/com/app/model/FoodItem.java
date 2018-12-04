package com.app.model;

public class FoodItem {
    private String name;
    private String classification;
    private String time;
    private String picturePath;

    public FoodItem(String name, String classification, String time, String picturePath) {
        this.name = name;
        this.classification = classification;
        this.time = time;
        this.picturePath = picturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
