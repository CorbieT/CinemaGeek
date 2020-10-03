package com.bignerdranch.android.cinemaquiz.ui.fragments.questionList;

import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseViewLoaderContract;

import java.util.List;

interface QuestionListContract {
    interface View<T> extends BaseViewLoaderContract {
        void setupRecycler(List<T> newItems);
        void showErrorLoadingQuestions(String msg);
    }

    interface Presenter {
        void loadQuestions();
        int getLastQuestionId();
    }
}
