package com.bignerdranch.android.cinemaquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Category;
import com.bignerdranch.android.cinemaquiz.utils.SingletonFonts;

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
    RecyclerView mCategoriesRecyclerView;

    private Unbinder unbinder;
    private CategoryAdapter mAdapter;
    private List<Category> mCategories;
    private boolean isComplete;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void updateUI() {
        mCategories = new ArrayList<>();
        setCategoriesTitle();
        mAdapter = new CategoryAdapter(mCategories);
        mCategoriesRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private FrameLayout mFrameLayout;

        @Override
        public void onClick(View view) {
            QuestionFragment fragment = QuestionFragment.newInstance(mTitleTextView.getText().toString());
            FragmentTransaction trans = Objects.requireNonNull(getFragmentManager()).beginTransaction();
            trans.replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
        }

        private CategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_categories_title_text_view);
            mFrameLayout = itemView.findViewById(R.id.list_container);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Category> mCategoryList;

        private CategoryAdapter(List<Category> categories) {
            mCategoryList = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_categories_fragment, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = mCategoryList.get(position);
            holder.mTitleTextView.setText(category.getTitle());
            holder.mTitleTextView.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());

            SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences(QuestionFragment.APP_TAG, Context.MODE_PRIVATE);
            isComplete = mPref.getBoolean(category.getTitle() + "_complete", false);
            if (isComplete)
                holder.mFrameLayout.setBackgroundResource(R.drawable.category_list_complete);
            else holder.mFrameLayout.setBackgroundResource(R.drawable.category_list);
        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }
    }

    private void setCategoriesTitle() {
        try {
            XmlPullParser parser = getResources().getXml(R.xml.text);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")) {
                    Category category = new Category();
                    category.setTitle(parser.getAttributeValue(0));
                    mCategories.add(category);
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

}
