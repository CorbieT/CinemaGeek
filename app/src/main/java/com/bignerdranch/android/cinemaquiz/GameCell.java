package com.bignerdranch.android.cinemaquiz;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

public class GameCell extends AppCompatButton{

    private char mGameSymbol;
    private boolean isClicked = false;
    private boolean isRightSymbol = false;

    public GameCell(Context context) {
        super(context);
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
