package com.bignerdranch.android.cinemaquiz.model;

import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;

public class Points {

    private static final int POINTS_FOR_WIN = 1;
    private static final int PRICE_FIRST_HINT = 10;
    private static final int POINTS_FOR_BONUS = 10;
    private static final int PRICE_SECOND_HINT = 1;
    private static final int POINTS_START_GAME = 13;
    private final SharedPrefHelper sharedPrefHelper;

    public Points(SharedPrefHelper sharedPrefHelper) {
        this.sharedPrefHelper = sharedPrefHelper;
    }

    public boolean isFirstHintAvailable() {
        return getCurrentPoints() >= PRICE_FIRST_HINT;
    }

    public boolean checkSecondHint() {
        return getCurrentPoints() >= PRICE_SECOND_HINT;
    }

    public void useFirstHint() {
        sharedPrefHelper.setPoints(getCurrentPoints() - PRICE_FIRST_HINT);
    }

    public void useSecondHint() {
        sharedPrefHelper.setPoints(getCurrentPoints() - PRICE_SECOND_HINT);
    }

    public void useBonusHint() {
        sharedPrefHelper.setPoints(getCurrentPoints() + POINTS_FOR_BONUS);
    }

    public void increasePoints() {
        sharedPrefHelper.setPoints(getCurrentPoints() + POINTS_FOR_WIN);
    }

    public int getCurrentPoints() {
        return sharedPrefHelper.getPoints(POINTS_START_GAME);
    }
}
