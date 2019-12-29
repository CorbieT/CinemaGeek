package com.bignerdranch.android.cinemaquiz.ui.fragments.categories;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.SharedPrefHelper;
import com.bignerdranch.android.cinemaquiz.model.Category;
import com.bignerdranch.android.cinemaquiz.repositories.categories.CategoryRepositoryImpl;
import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseFragment;
import com.bignerdranch.android.cinemaquiz.ui.fragments.categories.recycler.CategoryAdapter;
import com.bignerdranch.android.cinemaquiz.ui.fragments.question.QuestionFragment;
import com.bignerdranch.android.cinemaquiz.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends BaseFragment implements CategoriesContract.View<Category> {

    @BindView(R.id.fragment_categories_recycler_view)
    RecyclerView categoriesRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Unbinder unbinder;

    private CategoriesContract.Presenter presenter;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.categories_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (getContext() != null) {
            presenter = new CategoriesPresenter(this,
                    new CategoryRepositoryImpl(Utils.getXmlParser(getContext(), R.xml.text),
                            new SharedPrefHelper(getContext())),
                    getLifecycle());
        }
    }

    @Override
    public void setupRecycler(List<Category> newItems) {
        categoriesRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(categoriesRecyclerView.getContext(),
                R.anim.layout_animation_right));
        categoriesRecyclerView.setAdapter(new CategoryAdapter(getActivity(),
                newItems,
                s -> createFragmentWithBackStack(QuestionFragment.newInstance(s))));
    }

    @Override
    public void showErrorLoadingCategories(String msg) {
        Utils.showToast(getContext(), getString(R.string.error_msg, msg));
    }

    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
