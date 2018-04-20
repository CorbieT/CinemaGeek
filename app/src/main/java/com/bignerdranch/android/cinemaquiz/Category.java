package com.bignerdranch.android.cinemaquiz;

import java.util.UUID;

public class Category {

    private UUID mId;
    private String mTitle;

    public Category() {
        this(UUID.randomUUID());
    }

    public Category(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
