package com.bignerdranch.android.cinemaquiz.utils;

import androidx.annotation.NonNull;

public class Utils {
    public static String[] getSplitBySpacesString(String word) {
        return word.split("\\s+");
    }

    @NonNull
    public static String removeSpaces(String word) {
        return word.replaceAll("\\s", "").toUpperCase();
    }
}
