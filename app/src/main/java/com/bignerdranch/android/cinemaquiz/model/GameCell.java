package com.bignerdranch.android.cinemaquiz.model;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bignerdranch.android.cinemaquiz.R;

public class GameCell extends AppCompatTextView {

    private boolean isRightSymbol = false;

    public GameCell(Context context) {
        super(context);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        this.setMinEms(1);
        this.setTextSize(28);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        this.setBackgroundResource(R.drawable.rectangle_rounded);
    }

    public boolean isRightSymbol() {
        return isRightSymbol;
    }

    public void setRightSymbol(boolean rightSymbol) {
        isRightSymbol = rightSymbol;
    }

    public void showCell() {
        this.setVisibility(View.VISIBLE);
    }

    public boolean isVisible() {
        return this.getVisibility() == View.VISIBLE;
    }

    @Override
    public String getText() {
        return super.getText().toString();
    }

    public void hideCell() {
        this.setVisibility(View.INVISIBLE);
    }
}
