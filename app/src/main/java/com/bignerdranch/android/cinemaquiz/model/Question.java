package com.bignerdranch.android.cinemaquiz.model;

public class Question {

    private int id;
    private String questionText;
    private String answerText;

    public Question(int id, String questionText, String answerText) {
        this.id = id;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answerText;
    }

    public void setAnswer(String answer) {
        answerText = answer;
    }
}
