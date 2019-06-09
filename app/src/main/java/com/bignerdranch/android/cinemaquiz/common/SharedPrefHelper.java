package com.bignerdranch.android.cinemaquiz.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private final String APP_TAG = "cinema_quiz";
    private final String KEY_SOUND = "sound";
    private final String AD_COUNTER_TAG = "ad_counter";
    private final String KEY_POINTS = "points";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

    public void setKeySound(boolean keySound) {
        editor.putBoolean(KEY_SOUND, keySound).apply();
    }

    public boolean isKeySound() {
        return sharedPreferences.getBoolean(KEY_SOUND, false);
    }

    public int getAdCounter() {
        return sharedPreferences.getInt(AD_COUNTER_TAG, 0);
    }

    public void setAdCounter(int adCounter) {
        editor.putInt(AD_COUNTER_TAG, adCounter).apply();
    }

    public int getQuestionId(String categoryTitle) {
        return sharedPreferences.getInt(categoryTitle, 0);
    }

    public void setQuestionId(String categoryTitle, int questionId) {
        editor.putInt(categoryTitle, questionId).apply();
    }

    public boolean isCategoryComplete(String categoryTitle) {
        return sharedPreferences.getBoolean(categoryTitle + "_complete", false);
    }

    public void setCategoryComplete(String categoryTitle) {
        editor.putBoolean(categoryTitle + "_complete", true).apply();
    }

    public int getPoints(int defaultPoints) {
        return sharedPreferences.getInt(KEY_POINTS, defaultPoints);
    }

    public void setPoints(int points) {
        editor.putInt(KEY_POINTS, points).apply();
    }
}
