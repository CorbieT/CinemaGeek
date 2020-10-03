package com.bignerdranch.android.cinemaquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    private int id;
    private String questionText;
    private String answerText;

    public Question(int id, String questionText, String answerText) {
        this.id = id;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        questionText = in.readString();
        answerText = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.questionText);
        dest.writeString(this.answerText);
    }

}
