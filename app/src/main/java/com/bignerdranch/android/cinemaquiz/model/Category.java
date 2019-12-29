package com.bignerdranch.android.cinemaquiz.model;

public class Category {

    private String title;
    private Boolean isCompleted;

    public Category(String title, Boolean isCompleted) {
        this.title = title;
        this.isCompleted = isCompleted;
    }

    public String getTitle() {
        return title;
    }

    public Boolean isCompleted() {
        return isCompleted;
    }
}
