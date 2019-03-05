package com.bignerdranch.android.cinemaquiz.utils;

import android.content.Context;
import android.graphics.Typeface;

public class SingletonFonts {

    private static Typeface font1;
    private static Typeface font2;

    public Typeface getFont1() {
        return font1;
    }

    public Typeface getFont2() {
        return font2;
    }

    public static void setFont1(Typeface font1) {
        SingletonFonts.font1 = font1;
    }

    public static void setFont2(Typeface font2) {
        SingletonFonts.font2 = font2;
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
            setFont1(Typeface.createFromAsset(activity.getAssets(), "fonts/trixie.ttf"));
            setFont2(Typeface.createFromAsset(activity.getAssets(), "fonts/cuprum.ttf"));

        }
        return localInstance;
    }
}
