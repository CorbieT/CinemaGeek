package com.bignerdranch.android.cinemaquiz.model;

import android.content.Context;

import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.interfaces.QuestionContract;
import com.bignerdranch.android.cinemaquiz.repositories.QuestionRepository;
import com.bignerdranch.android.cinemaquiz.specifications.QuestionsByCategorySpecification;

import java.util.List;

public class QuestionModel implements QuestionContract.Model {

    private String category;

    private Points points;
    private SharedPrefHelper sharedPrefHelper;
    private QuestionRepository questionRepository;

    public QuestionModel(Context context, String category) {
        this.category = category;
        this.points = new Points(context);
        this.sharedPrefHelper = new SharedPrefHelper(context);
        this.questionRepository = new QuestionRepository(context);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.query(new QuestionsByCategorySpecification(category));
    }

    @Override
    public int getQuestionId() {
        return sharedPrefHelper.getQuestionId(category);
    }

    @Override
    public void setQuestionId(int qId) {
        sharedPrefHelper.setQuestionId(category, qId);
    }

    @Override
    public int getAdCounter() {
        return sharedPrefHelper.getAdCounter();
    }

    @Override
    public void setAdCounter(int count) {
        sharedPrefHelper.setAdCounter(count);
    }

    @Override
    public boolean canUseFirstHint() {
        return points.checkForFirstHint();
    }

    @Override
    public boolean canUseSecondHint() {
        return points.checkForSecondHint();
    }

    @Override
    public void setPointsForFirstHint() {
        points.useFirstHint();
    }

    @Override
    public void setPointsForSecondHint() {
        points.useSecondHint();
    }

    @Override
    public void setPointsForBonus() {
        points.useBonusHint();
    }

    @Override
    public void setPointsForWin() {
        points.addPointsForWin();
    }

    @Override
    public int getCurrentPoints() {
        return points.getCurrentPoints();
    }

    @Override
    public void setCategoryComplete() {
        sharedPrefHelper.setCategoryComplete(category);
    }

}
