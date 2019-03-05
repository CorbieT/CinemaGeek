package com.bignerdranch.android.cinemaquiz.model;

import java.util.UUID;

public class Question {

    private UUID mId;
    private String mQuestionText;
    private String mAnswer;

    public Question() {
        this(UUID.randomUUID());
    }

    public Question(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public void setQuestionText(String questionText) {
        mQuestionText = questionText;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }
}
