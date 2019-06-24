package com.bignerdranch.android.cinemaquiz.model;

import java.util.UUID;

public class Category {

    private UUID id;
    private String mTitle;

    public Category() {
        this(UUID.randomUUID());
    }

    public Category(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
