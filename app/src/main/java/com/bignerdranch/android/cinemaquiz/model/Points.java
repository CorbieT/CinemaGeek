package com.bignerdranch.android.cinemaquiz.model;

import android.content.Context;

import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;

public class Points {

    private static final int POINTS_FOR_WIN = 2000;
    private static final int PRICE_FIRST_HINT = 10;
    private static final int POINTS_FOR_BONUS = 10;
    private static final int PRICE_SECOND_HINT = 1;
    private static final int POINTS_START_GAME = 13;
    private final SharedPrefHelper sharedPrefHelper;

    public Points(Context context) {
        this.sharedPrefHelper = new SharedPrefHelper(context);
    }

    private void addPoints(int points) {
        sharedPrefHelper.setPoints(getCurrentPoints() + points);
    }

    private void removePoints(int points) {
        sharedPrefHelper.setPoints(getCurrentPoints() - points);
    }

    public int getCurrentPoints() {
        return sharedPrefHelper.getPoints(POINTS_START_GAME);
    }

    public boolean checkForFirstHint() {
        return getCurrentPoints() >= PRICE_FIRST_HINT;
    }

    public boolean checkForSecondHint() {
        return getCurrentPoints() >= PRICE_SECOND_HINT;
    }

    public void useFirstHint() {
        removePoints(PRICE_FIRST_HINT);
    }

    public void useSecondHint() {
        removePoints(PRICE_SECOND_HINT);
    }

    public void useBonusHint() {
        addPoints(POINTS_FOR_BONUS);
    }

    public void addPointsForWin() {
        addPoints(POINTS_FOR_WIN);
    }
}
