package com.bignerdranch.android.cinemaquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.adapters.CategoryAdapter;
import com.bignerdranch.android.cinemaquiz.model.Category;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoriesFragment extends Fragment {

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
        categoriesRecyclerView.setAdapter(new CategoryAdapter(getActivity(), getCategoriesFromXml(), s -> {
            QuestionFragment fragment = QuestionFragment.newInstance(s);
            FragmentTransaction trans = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSupportFragmentManager()).beginTransaction();
            trans.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
        }));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private List<Category> getCategoriesFromXml() {
        List<Category> categories = new ArrayList<>();
        try {
            XmlPullParser parser = getResources().getXml(R.xml.text);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")) {
                    Category category = new Category();
                    category.setTitle(parser.getAttributeValue(0));
                    categories.add(category);
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
        return categories;
    }
}
