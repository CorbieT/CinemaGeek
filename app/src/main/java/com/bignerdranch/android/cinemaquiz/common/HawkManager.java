package com.bignerdranch.android.cinemaquiz.common;

import com.orhanobut.hawk.Hawk;

public class HawkManager {

    private final String KEY_SOUND = "sound";
    private final String AD_COUNTER_TAG = "ad_counter";
    private final String KEY_POINTS = "points";

    private static class HawkManagerHolder {
        private static final HawkManager MANAGER_INSTANCE = new HawkManager();
    }

    public static HawkManager getInstance() {
        return HawkManagerHolder.MANAGER_INSTANCE;
    }

    public void setKeySound(boolean keySound) {
        Hawk.put(KEY_SOUND, keySound);
    }

    public boolean isKeySound() {
        return Hawk.contains(KEY_SOUND) && (boolean) Hawk.get(KEY_SOUND);
    }

    public int getAdCounter() {
        return Hawk.get(AD_COUNTER_TAG, 0);
    }

    public void setAdCounter(int adCounter) {
        Hawk.put(AD_COUNTER_TAG, adCounter);
    }

    public int getQuestionId(String categoryTitle) {
        return Hawk.get(categoryTitle, 0);
    }

    public void setQuestionId(String categoryTitle, int questionId) {
        Hawk.put(categoryTitle, questionId);
    }

    public boolean isCategoryComplete(String categoryTitle) {
        return Hawk.contains(categoryTitle + "_complete") && Hawk.get(categoryTitle + "_complete", false);
    }

    public void setCategoryComplete(String categoryTitle) {
        Hawk.put(categoryTitle + "_complete", true);
    }

    public int getPoints(int defaultPoints) {
        return Hawk.get(KEY_POINTS, defaultPoints);
    }

    public void setPoints(int points) {
        Hawk.put(KEY_POINTS, points);
    }

}
