package com.bignerdranch.android.cinemaquiz.model;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;

public class AnswerCell extends AppCompatTextView {

    private String correctSymbol;
    private int gameCellPickedId;
    private boolean isPrompted;

    public AnswerCell(Context context) {
        super(context);
    }

    public AnswerCell(Context context, String correctSymbol) {
        super(context);
        this.correctSymbol = correctSymbol;
        this.setMinEms(1);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        this.setBackgroundResource(R.drawable.bottom_stroke);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        this.setText("");
    }

    @Override
    public String getText() {
        return super.getText().toString();
    }

    public boolean isEmpty() {
        return this.getText().isEmpty();
    }

    public String getCorrectSymbol() {
        return correctSymbol;
    }

    public void showCorrectSymbol() {
        this.setText(correctSymbol);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.correct_color));
        this.setBackgroundResource(R.drawable.bottom_stroke_green);
        this.isPrompted = true;
    }

    public boolean isCorrectSymbol() {
        if (isEmpty()) return false;
        return this.getText().equals(correctSymbol);
    }

    public void clearAnswerCell() {
        this.setText("");
    }

    public int getGameCellPickedId() {
        return gameCellPickedId;
    }

    public void setGameCellPickedId(int gameCellPickedId) {
        this.gameCellPickedId = gameCellPickedId;
    }

    public boolean isPrompted() {
        return isPrompted;
    }
}
