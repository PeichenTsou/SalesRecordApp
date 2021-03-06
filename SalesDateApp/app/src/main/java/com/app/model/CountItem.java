package com.app.model;

public class CountItem {
    private String name;
    private String classification;
    private String time;
    private String count;
    //add period
    private String period;
    //add date
    private String date;

    //add period & date
    public CountItem(String date, String period, String name, String classification, String time, String count) {
        this.name = name;
        this.classification = classification;
        this.time = time;
        this.count = count;
        //add period
        this.period = period;
        //add date
        this.date = date;
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

    //add period
    public String getPeriod() { return period; }
    //add date
    public String getDate() { return date; }
}

//public class CountItem {
//    private String name;
//    private String classification;
//    private String time;
//    private String count;
//    //add period
//    private String period;
//    //add date
//    private String date;
//
//    //add period & date
//    public CountItem(String date, String period, String name, String classification, String time, String count) {
//        this.name = name;
//        this.classification = classification;
//        this.time = time;
//        this.count = count;
//        //add period
//        this.period = period;
//        //add date
//        this.date = date;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getClassification() {
//        return classification;
//    }
//
//    public void setClassification(String classification) {
//        this.classification = classification;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getCount() {
//        return count;
//    }
//
//    public void setCount(String count) {
//        this.count = count;
//    }
//
//    //add period
//    public String getPeriod() { return period; }
//    //add date
//    public String getDate() { return date; }
//}
