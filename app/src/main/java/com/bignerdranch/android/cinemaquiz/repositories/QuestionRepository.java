package com.bignerdranch.android.cinemaquiz.repositories;

import android.content.Context;

import com.bignerdranch.android.cinemaquiz.common.XmlPullParserHelper;
import com.bignerdranch.android.cinemaquiz.interfaces.Repository;
import com.bignerdranch.android.cinemaquiz.interfaces.Specification;
import com.bignerdranch.android.cinemaquiz.interfaces.XmlSpecification;
import com.bignerdranch.android.cinemaquiz.model.Question;

import java.util.List;

public class QuestionRepository implements Repository<Question> {

    private Context context;

    public QuestionRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<Question> query(Specification specification) {
        final XmlSpecification xmlSpecification = (XmlSpecification) specification;
        return XmlPullParserHelper.getQuestionsFromXMLByCategoryTitle(context, xmlSpecification.getCategory());
    }
}
