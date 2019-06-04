package com.bignerdranch.android.cinemaquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.adapters.CategoryAdapter;
import com.bignerdranch.android.cinemaquiz.common.XmlPullParserHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends BaseFragment {

    @BindView(R.id.fragment_categories_recycler_view)
    RecyclerView categoriesRecyclerView;

    private Unbinder unbinder;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(categoriesRecyclerView.getContext(), R.anim.layout_animation));
        categoriesRecyclerView.setAdapter(new CategoryAdapter(getActivity(),
                XmlPullParserHelper.getCategoriesFromXml(getActivity()),
                s -> createFragmentWithBackStack(QuestionFragment.newInstance(s), null)));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
