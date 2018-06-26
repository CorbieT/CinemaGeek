package com.bignerdranch.android.cinemaquiz;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;

public class GameCell extends AppCompatTextView{

    private char mGameSymbol;
    private boolean isClicked = false;
    private boolean isRightSymbol = false;

    public GameCell(Context context) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setMinEms(1);
        setTextSize(28);
        setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        this.setBackgroundResource(R.drawable.rectangle_rounded);
    }

    public char getGameSymbol() {
        return mGameSymbol;
    }

    public void setGameSymbol(char gameSymbol) {
        mGameSymbol = gameSymbol;
        this.setText(String.valueOf(mGameSymbol));
    }

    public boolean isRightSymbol() {
        return isRightSymbol;
    }

    public void setRightSymbol(boolean rightSymbol) {
        isRightSymbol = rightSymbol;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public void showCell(){
        this.setVisibility(View.VISIBLE);
        isClicked = false;
    }

    public void hideCell(){
        if(!isClicked)
            this.setVisibility(View.INVISIBLE);
    }
}
