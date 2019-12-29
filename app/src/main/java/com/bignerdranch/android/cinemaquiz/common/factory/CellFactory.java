package com.bignerdranch.android.cinemaquiz.common.factory;

import android.content.Context;

import com.bignerdranch.android.cinemaquiz.view.cell.AnswerCell;
import com.bignerdranch.android.cinemaquiz.view.cell.Cell;
import com.bignerdranch.android.cinemaquiz.view.cell.CellType;
import com.bignerdranch.android.cinemaquiz.view.cell.DashCell;
import com.bignerdranch.android.cinemaquiz.view.cell.GameCell;

public class CellFactory {
    private Context context;

    public CellFactory(Context context) {
        this.context = context;
    }

    public Cell createCell(CellType cellType) {
        switch (cellType) {
            case ANSWER: return new AnswerCell(context);
            case GAME: return new GameCell(context);
            case DASH: return new DashCell(context);
            default: return null;
        }
    }
}
