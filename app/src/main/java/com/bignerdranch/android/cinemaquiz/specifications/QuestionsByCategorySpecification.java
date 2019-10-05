package com.bignerdranch.android.cinemaquiz.specifications;

import com.bignerdranch.android.cinemaquiz.interfaces.XmlSpecification;

public class QuestionsByCategorySpecification implements XmlSpecification {
    private String category;

    public QuestionsByCategorySpecification(String category) {
        this.category = category;
    }

    @Override
    public String getCategory() {
        return category;
    }
}
