package com.bignerdranch.android.cinemaquiz.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class Utils {
    public static String[] getStringSplittedBySpace(String word) {
        return word.split("\\s+");
    }

    @NonNull
    public static String removeSpaces(String word) {
        return word.replaceAll("\\s", "").toUpperCase();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static XmlResourceParser getXmlParser(Context context, int xmlId) {
        return context.getResources().getXml(xmlId);
    }
}
