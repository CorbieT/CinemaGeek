package com.bignerdranch.android.cinemaquiz.model;

public class Question {

    private int mId;
    private String mQuestionText;
    private String mAnswer;

    public Question(int mId, String mQuestionText, String mAnswer) {
        this.mId = mId;
        this.mQuestionText = mQuestionText;
        this.mAnswer = mAnswer;
    }

    public int getId() {
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
