package com.bignerdranch.android.cinemaquiz.model;

import com.bignerdranch.android.cinemaquiz.common.HawkManager;

public class Points {

    private static final int POINTS_FOR_WIN = 2;
    private static final int PRICE_FIRST_HINT = 10;
    private static final int POINTS_FOR_BONUS = 10;
    private static final int PRICE_SECOND_HINT = 1;
    private static final int POINTS_START_GAME = 13;
    private final HawkManager hawkManager = HawkManager.getInstance();

    public boolean checkFirstHint() {
        return getCurrentPoints() >= PRICE_FIRST_HINT;
    }

    public boolean checkSecondHint() {
        return getCurrentPoints() >= PRICE_SECOND_HINT;
    }

    public void useFirstHint() {
        hawkManager.setPoints(getCurrentPoints() - PRICE_FIRST_HINT);
    }

    public void useSecondHint() {
        hawkManager.setPoints(getCurrentPoints() - PRICE_SECOND_HINT);
    }

    public void useBonusHint() {
        hawkManager.setPoints(getCurrentPoints() + POINTS_FOR_BONUS);
    }

    public void increasePoints() {
        hawkManager.setPoints(getCurrentPoints() + POINTS_FOR_WIN);
    }

    public int getCurrentPoints() {
        return hawkManager.getPoints(POINTS_START_GAME);
    }
}
