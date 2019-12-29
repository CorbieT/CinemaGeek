package com.bignerdranch.android.cinemaquiz.repositories.questions;

import android.content.res.XmlResourceParser;

import com.bignerdranch.android.cinemaquiz.common.XmlPullParserHelper;
import com.bignerdranch.android.cinemaquiz.model.Question;

import java.util.List;

import io.reactivex.Single;

public class QuestionRepositoryImpl implements QuestionRepository<Question> {
    private XmlPullParserHelper xmlPullParserHelper;
    private String categoryTitle;

    public QuestionRepositoryImpl(XmlResourceParser parser, String categoryTitle) {
        this.xmlPullParserHelper = new XmlPullParserHelper(parser);
        this.categoryTitle = categoryTitle;
    }

    @Override
    public Single<List<Question>> parseQuestions() {
        return xmlPullParserHelper.getQuestionsFromXMLByCategoryTitle(categoryTitle);
    }
}
