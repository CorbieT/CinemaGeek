package com.bignerdranch.android.cinemaquiz.ui.fragments.questionList;

import androidx.lifecycle.LifecycleObserver;

import com.bignerdranch.android.cinemaquiz.common.BasePresenter;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.repositories.questions.QuestionRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class QuestionListPresenter extends BasePresenter implements QuestionListContract.Presenter, LifecycleObserver {

    private QuestionListContract.View<Question> view;
    private QuestionRepository questionRepository;
    private SharedPrefHelper sharedPrefHelper;
    private String category;

    public QuestionListPresenter(QuestionListContract.View<Question> view,
                                 QuestionRepository questionRepository,
                                 SharedPrefHelper sharedPrefHelper,
                                 String category) {
        this.view = view;
        this.questionRepository = questionRepository;
        this.sharedPrefHelper = sharedPrefHelper;
        this.category = category;
    }

    @Override
    public void loadQuestions() {
        disposables.add(questionRepository.parseQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> view.showLoader())
                .doFinally(() -> view.hideLoader())
                .subscribe(response -> view.setupRecycler(response),
                        error -> view.showErrorLoadingQuestions("вопросов")));
    }

    @Override
    public int getLastQuestionId() {
        return sharedPrefHelper.getQuestionId(category);
    }
}
