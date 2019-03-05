package com.bignerdranch.android.cinemaquiz.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.Gravity;

import com.bignerdranch.android.cinemaquiz.R;

public class AnswerCell extends AppCompatTextView{

    private static final char EMPTY = ' ';

    private GameCell mGameCell = null;
    private char mAnswerSymbol;
    private char mCorrectSymbol;

    public AnswerCell(Context context) {
        super(context);
    }

    public AnswerCell(Context context, char correctSymbol, int wordLength) {
        super(context);
        setMinEms(1);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        setBackgroundResource(R.drawable.bottom_stroke);
        mCorrectSymbol = correctSymbol;

        if (wordLength > 13) setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        else setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        setEmpty();
    }

    private void setEmpty(){
        mAnswerSymbol = EMPTY;
        setText(String.valueOf(mAnswerSymbol));
    }

    public boolean isEmpty(){
        return mAnswerSymbol == EMPTY;
    }

    public char getAnswerSymbol() {
        return mAnswerSymbol;
    }

    public void setAnswerSymbol(GameCell gameCell) {
        mGameCell = gameCell;
        mAnswerSymbol = gameCell.getGameSymbol();
        this.setText(String.valueOf(mAnswerSymbol));
    }

    public void showCorrectSymbol(){
        mAnswerSymbol = mCorrectSymbol;
        setText(String.valueOf(mAnswerSymbol));
        setTextColor(ContextCompat.getColor(getContext(), R.color.isClickedButton));
        setBackgroundResource(R.drawable.bottom_stroke_green);
    }

    public boolean compareAnswerSymbols(){
        return mAnswerSymbol == mCorrectSymbol;
    }

    public void clearAnswerCell(){
        if (this.mGameCell != null){
            this.mGameCell.showCell();
            setEmpty();
            this.mGameCell = null;
        }
    }

    public GameCell getGameCell() {
        return mGameCell;
    }

    public char getCorrectSymbol() {
        return mCorrectSymbol;
    }
}
