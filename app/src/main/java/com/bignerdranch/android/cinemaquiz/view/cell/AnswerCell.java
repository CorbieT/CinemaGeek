package com.bignerdranch.android.cinemaquiz.view.cell;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;

public class AnswerCell extends Cell {

    private String correctSymbol;
    private int gameCellPickedId;
    private boolean isPrompted;

    public AnswerCell(Context context) {
        super(context);
        this.setMinEms(1);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        this.setBackgroundResource(R.drawable.bottom_stroke);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
    }

    public boolean isEmpty() {
        return this.getSymbol().isEmpty();
    }

    public String getCorrectSymbol() {
        return correctSymbol;
    }

    public void showCorrectSymbol() {
        this.setSymbol(correctSymbol);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_color));
        this.setBackgroundResource(R.drawable.bottom_stroke_green);
        this.isPrompted = true;
    }

    public void setCorrectSymbol(String correctSymbol) {
        this.correctSymbol = correctSymbol;
    }

    public boolean isCorrectSymbol() {
        if (isEmpty()) return false;
        return this.getSymbol().equals(correctSymbol);
    }

    public void clear() {
        this.setSymbol("");
    }

    public int getGameCellPickedId() {
        return gameCellPickedId;
    }

    public void setGameCellPickedId(int gameCellPickedId) {
        this.gameCellPickedId = gameCellPickedId;
    }

    public boolean isNotPrompted() {
        return !isPrompted;
    }
}
