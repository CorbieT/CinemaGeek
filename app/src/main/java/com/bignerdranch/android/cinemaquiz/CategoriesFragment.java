package com.bignerdranch.android.cinemaquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
 
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment{

    private RecyclerView mCategoriesRecyclerView;
    private CategoryAdapter mAdapter;
    private List<Category> mCategories;
    private boolean isComplete;

    public static CategoriesFragment newInstance(){
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.categories_fragment, container, false);
        mCategoriesRecyclerView = view.findViewById(R.id.fragment_categories_recycler_view);
        mCategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI(){
        mCategories = new ArrayList<>();
        setCategoriesTitle();
        mAdapter = new CategoryAdapter(mCategories);
        mCategoriesRecyclerView.setAdapter(mAdapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;

        @Override
        public void onClick(View view){
            QuestionFragment fragment = QuestionFragment.newInstance(mTitleTextView.getText().toString());
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.replace(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
        }

        private CategoryHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_categories_title_text_view);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder>{
        private List<Category> mCategoryList;

        private CategoryAdapter(List<Category> categories){
            mCategoryList = categories;
        }

        @Override
        public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_categories_fragment, parent, false);
            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryHolder holder, int position){
            Category category = mCategoryList.get(position);
            holder.mTitleTextView.setText(category.getTitle());
            holder.mTitleTextView.setTypeface(SingletonFonts.getInstance(getActivity()).getFont1());

            SharedPreferences mPref = getActivity().getSharedPreferences(QuestionFragment.APP_TAG, Context.MODE_PRIVATE);
            isComplete = mPref.getBoolean(category.getTitle() + "_complete", false);
            if (isComplete) holder.mTitleTextView.setBackgroundResource(R.drawable.category_list_complete);
            else holder.mTitleTextView.setBackgroundResource(R.drawable.category_list);
        }

        @Override
        public int getItemCount(){
            return mCategoryList.size();
        }
    }

    private void setCategoriesTitle(){
        try{
            XmlPullParser parser = getResources().getXml(R.xml.text);
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("category")){
                    Category category = new Category();
                    category.setTitle(parser.getAttributeValue(0));
                    mCategories.add(category);
                }
                parser.next();
            }
        } catch (Throwable t){
            Toast.makeText(getActivity(), "Error loading XML document: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

}
