package com.bignerdranch.android.cinemaquiz.common;

import com.orhanobut.hawk.Hawk;

public class HawkManager {

    private static final String KEY_SOUND = "sound";

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
}
