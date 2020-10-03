package com.bignerdranch.android.cinemaquiz.common;

import android.content.res.XmlResourceParser;

import com.bignerdranch.android.cinemaquiz.model.Category;
import com.bignerdranch.android.cinemaquiz.model.Question;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class XmlPullParserHelper {
    private XmlResourceParser parser;
    private SharedPrefHelper sharedPrefHelper;

    public XmlPullParserHelper(XmlResourceParser parser) {
        this.parser = parser;
    }

    public XmlPullParserHelper(XmlResourceParser parser, SharedPrefHelper sharedPrefHelper) {
        this.parser = parser;
        this.sharedPrefHelper = sharedPrefHelper;
    }

    public Single<List<Category>> getCategoriesFromXml() {
        return Single.create(emitter -> {
            List<Category> categories = new ArrayList<>();
            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG
                            && parser.getName().equals("category")) {
                        String title = parser.getAttributeValue(0);
                        categories.add(new Category(title, sharedPrefHelper.isCategoryComplete(title)));
                    }
                    parser.next();
                }
            } catch (Throwable t) {
                emitter.onError(t);
            }
            emitter.onSuccess(categories);
        });
    }

    public Single<List<Question>> getQuestionsFromXMLByCategoryTitle(String categoryTitle) {
        return Single.create(emitter -> {
            List<Question> questions = new ArrayList<>();
            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG
                            && parser.getName().equals("category")
                            && parser.getAttributeValue(0).equals(categoryTitle)) {
                        for (int i = 0; i <= 49; i++) {
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
                emitter.onError(t);
            }
            emitter.onSuccess(questions);
        });
    }
}
