package com.bignerdranch.android.cinemaquiz;

import android.content.SharedPreferences;

public class Points {

    private SharedPreferences mPref;

    private static final int POINTS_FOR_WIN = 2;
    private static final int PRICE_FIRST_HINT = 10;
    private static final int POINTS_FOR_BONUS = 10;
    private static final int PRICE_SECOND_HINT = 1;
    private static final int POINTS_START_GAME = 13;
    private static final String KEY_POINTS = "points";

    public Points(SharedPreferences sharedPreferences){
        mPref = sharedPreferences;
    }

    public boolean checkFirstHint(){
        return getCurrentPoints() >= PRICE_FIRST_HINT;
    }

    public boolean checkSecondHint(){
        return getCurrentPoints() >= PRICE_SECOND_HINT;
    }

    public void useFirstHint(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(KEY_POINTS, getCurrentPoints() - PRICE_FIRST_HINT).apply();
    }

    public void useSecondHint(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(KEY_POINTS, getCurrentPoints() - PRICE_SECOND_HINT).apply();
    }

    public void useBonusHint(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(KEY_POINTS, getCurrentPoints() + POINTS_FOR_BONUS).apply();
    }

    public void increasePoints(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(KEY_POINTS, getCurrentPoints() + POINTS_FOR_WIN).apply();
    }

    public int getCurrentPoints(){
        return mPref.getInt(KEY_POINTS, POINTS_START_GAME);
    }
}
