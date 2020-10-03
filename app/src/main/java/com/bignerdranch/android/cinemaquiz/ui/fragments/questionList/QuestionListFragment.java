package com.bignerdranch.android.cinemaquiz.ui.fragments.questionList;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.repositories.questions.QuestionRepositoryImpl;
import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.question.QuestionFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.question.viewPager.QuestionPagerFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.questionList.recycler.QuestionListAdapter;
import com.bignerdranch.android.cinemaquiz.utils.Utils;
import com.bignerdranch.android.cinemaquiz.view.recycler.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bignerdranch.android.cinemaquiz.common.Constants.CATEGORY_TAG;

public class QuestionListFragment extends BaseFragment implements QuestionListContract.View<Question> {

    @BindView(R.id.question_list_recycler)
    RecyclerView questionListRecycler;

    @BindView(R.id.question_list_progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;

    private String categoryTitle = "";

    private QuestionListContract.Presenter presenter;
    private QuestionListAdapter questionListAdapter;

    public static QuestionListFragment newInstance(String category) { //FIXME убрать в BaseFragment
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_TAG, category);
        QuestionListFragment fragment = new QuestionListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        loadBundle(getArguments());
        presenter = new QuestionListPresenter(this,
                new QuestionRepositoryImpl(Utils.getXmlParser(Objects.requireNonNull(getContext()),
                        R.xml.text),
                        categoryTitle),
                new SharedPrefHelper(Objects.requireNonNull(getActivity())),
                categoryTitle);
        presenter.loadQuestions();
    }

    private void loadBundle(Bundle bundle) {
        if (bundle != null) {
            categoryTitle = bundle.getString(CATEGORY_TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.question_list_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (questionListAdapter != null && presenter != null) {
            questionListAdapter.updateLastQuestionId(presenter.getLastQuestionId());
        }
    }

    @Override
    public void setupRecycler(List<Question> newItems) {
        questionListRecycler.setLayoutManager(new GridLayoutManager(getActivity(),
                3,
                GridLayoutManager.VERTICAL,
                false));
        questionListRecycler.addItemDecoration(new GridSpacingItemDecoration(3,
                50,
                true));
        questionListAdapter = new QuestionListAdapter(newItems,
                questionId -> createFragmentWithBackStack(QuestionPagerFragment.newInstance(questionId,
                        new ArrayList<>(newItems), categoryTitle)),
                presenter.getLastQuestionId());
        questionListRecycler.setAdapter(questionListAdapter);
    }

    @Override
    public void showErrorLoadingQuestions(String msg) {
        Utils.showToast(getContext(),
                getString(R.string.error_msg, msg));
    }

    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }
}
