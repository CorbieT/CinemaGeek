package com.bignerdranch.android.cinemaquiz.view.cell;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;

import com.bignerdranch.android.cinemaquiz.R;

public class DashCell extends Cell {
    public DashCell(Context context) {
        super(context);
        this.setVisibility(View.VISIBLE);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        this.setMinEms(1);
        this.setTextColor(getResources().getColor(R.color.textColor));
        this.setText("—");
        this.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.setTypeface(null, Typeface.BOLD);
    }
}
