package com.bignerdranch.android.cinemaquiz.common;

import android.widget.LinearLayout;

public class Constants {
    public static final String ALPHABET = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
    public static final int ALPHABET_SIZE = ALPHABET.length();
    public static final int MAX_CELLS_COUNT = 18;

    public static final int WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT;
    public static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    public static final String CATEGORY_TAG = "CATEGORY_TITLE";
    public static final String QUESTION_ID = "QUESTION_ID";
    public static final String QUESTIONS = "QUESTIONS";
    public static final String QUESTION = "QUESTION";

    public class CategoryTitles {
        public static final String HORROR = "УЖАСЫ";
        public static final String COMEDY = "КОМЕДИИ";
        public static final String SERIES = "СЕРИАЛЫ";
        public static final String CARTOONS = "МУЛЬТФИЛЬМЫ";
        public static final String DRAMA = "DRAMA";
        public static final String PUZZLE = "ГОЛОВОЛОМКИ";
        public static final String RUSSIAN = "РОССИЙСКИЕ/СССР";
        public static final String SALTWORT = "СОЛЯНКА";
        public static final String SUPER = "СУПЕР";
        public static final String SALTWORT_2 = "СОЛЯНКА-2";
        public static final String ACTORS = "АКТЕРСКИЙ СОСТАВ";
        public static final String CINEMA_GEEK = "КИНОГИК";
        public static final String SO_SPEAK = "ТАК ГОВОРИЛ";
    }
}
