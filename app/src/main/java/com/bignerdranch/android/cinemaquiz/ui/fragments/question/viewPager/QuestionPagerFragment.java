package com.bignerdranch.android.cinemaquiz.ui.fragments.question.viewPager;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bignerdranch.android.cinemaquiz.common.Constants.CATEGORY_TAG;
import static com.bignerdranch.android.cinemaquiz.common.Constants.QUESTIONS;
import static com.bignerdranch.android.cinemaquiz.common.Constants.QUESTION_ID;

public class QuestionPagerFragment extends BaseFragment {

    @BindView(R.id.question_pager)
    ViewPager2 questionPager;

    private Unbinder unbinder;

    private QuestionFragmentStateAdapter questionStateAdapter;

    private int initQuestionId;
    private List<Question> questions;
    private String category;

    public static QuestionPagerFragment newInstance(int initQuestionId, ArrayList<Question> questions, String category) {
        Bundle bundle = new Bundle();
        bundle.putInt(QUESTION_ID, initQuestionId);
        bundle.putParcelableArrayList(QUESTIONS, questions);
        bundle.putString(CATEGORY_TAG, category);
        QuestionPagerFragment fragment = new QuestionPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        loadBundle(getArguments());
        questionStateAdapter = new QuestionFragmentStateAdapter(this, questions, category);
        questionPager.setAdapter(questionStateAdapter);
        questionPager.setCurrentItem(initQuestionId, false);
    }

    private void loadBundle(Bundle bundle) {
        if (bundle != null) {
            initQuestionId = bundle.getInt(QUESTION_ID);
            questions = bundle.getParcelableArrayList(QUESTIONS);
            category = bundle.getString(CATEGORY_TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.question_pager_fragment;
    }

    public void onNextQuestion(int questionId) {
        questionPager.setCurrentItem(questionId);
    }
}
