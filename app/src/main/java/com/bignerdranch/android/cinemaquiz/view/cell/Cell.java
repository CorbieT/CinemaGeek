package com.bignerdranch.android.cinemaquiz.view.cell;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;

public abstract class Cell extends AppCompatTextView {
    private String symbol = "";
    protected int id = 0;

    public Cell(Context context) {
        super(context);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
        this.setText(symbol);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
