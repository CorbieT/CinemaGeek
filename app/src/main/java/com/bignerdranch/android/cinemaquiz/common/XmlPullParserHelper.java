package com.bignerdranch.android.cinemaquiz.common;

import android.content.Context;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Category;
import com.bignerdranch.android.cinemaquiz.model.Question;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class XmlPullParserHelper {

    public static String getFaqFromXml(Context context) {
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.faq);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("faq")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        return parser.getText();
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context, "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
        return "";
    }

    public static String getRulesFromXml(Context context) {
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.rules);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("rules")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        return parser.getText();
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context, "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        } return "";
    }

    public static List<Category> getCategoriesFromXml(Context context) {
        List<Category> categories = new ArrayList<>();
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.text);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")) {
                    Category category = new Category();
                    category.setTitle(parser.getAttributeValue(0));
                    categories.add(category);
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context, "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
        return categories;
    }

    public static List<Question> getQuestionsFromXMLByCategoryTitle(Context context, String categoryTitle) {
        List<Question> questions = new ArrayList<>();
        try {
            XmlPullParser parser = context.getResources().getXml(R.xml.text);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")
                        && parser.getAttributeValue(0).equals(categoryTitle)) {
                    for (int i = 1; i <= 50; i++) {
                        while (!(parser.getEventType() == XmlPullParser.START_TAG
                                && parser.getName().equals("question")
                                && parser.getAttributeValue(0).equals(String.valueOf(i)))) {
                            parser.next();
                        }
                        String questionText = "";
                        String questionAnswer = "";
                        if (parser.next() == XmlPullParser.START_TAG
                                && parser.getName().equals("text")) {
                            if (parser.next() == XmlPullParser.TEXT) {
                                questionText = parser.getText();
                            }
                        }
                        while (!(parser.getEventType() == XmlPullParser.START_TAG
                                && parser.getName().equals("answer"))) {
                            parser.next();
                        }
                        if (parser.next() == XmlPullParser.TEXT) {
                            questionAnswer = parser.getText();
                        }
                        questions.add(new Question(i, questionText, questionAnswer));
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(context, "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
        return questions;
    }
}
