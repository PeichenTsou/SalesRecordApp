package com.app.model;

public class CountItem {
    private String name;
    private String classification;
    private String time;
    private String count;

    public CountItem(String name, String classification, String time, String count) {
        this.name = name;
        this.classification = classification;
        this.time = time;
        this.count = count;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
