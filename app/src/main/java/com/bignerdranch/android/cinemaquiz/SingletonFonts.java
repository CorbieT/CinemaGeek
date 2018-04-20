package com.bignerdranch.android.cinemaquiz;

import android.content.Context;
import android.graphics.Typeface;

public class SingletonFonts {

    private static Typeface font1;

    public Typeface getFont1() {
        return font1;
    }

    public static void setFont1(Typeface font1) {
        SingletonFonts.font1 = font1;
    }

    private static volatile SingletonFonts instance;

    private SingletonFonts() {}

    public static SingletonFonts getInstance(Context activity) {
        SingletonFonts localInstance = instance;
        if (localInstance == null) {
            synchronized (SingletonFonts.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SingletonFonts();
                }
            }
            setFont1(Typeface.createFromAsset(activity.getAssets(), "fonts/cuprum.ttf"));

        }
        return localInstance;
    }
}
