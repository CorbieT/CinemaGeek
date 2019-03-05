package com.bignerdranch.android.cinemaquiz.fragments;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.cinemaquiz.model.Category;
import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.utils.SingletonFonts;

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
        private FrameLayout mFrameLayout;
        private ImageView mImageView;

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
            mFrameLayout = itemView.findViewById(R.id.list_container);
            mImageView = itemView.findViewById(R.id.category_icon);
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
            if(isComplete) holder.mFrameLayout.setBackgroundResource(R.drawable.category_list_complete);
            else holder.mFrameLayout.setBackgroundResource(R.drawable.category_list);
            setCategoryIcon(category, holder.mImageView);
        }

        @Override
        public int getItemCount(){
            return mCategoryList.size();
        }
    }

    private void setCategoryIcon(Category category, ImageView imageView){
        switch (category.getTitle()){
            case "УЖАСЫ": imageView.setImageResource(R.drawable.horror);
                break;
            case "КОМЕДИИ": imageView.setImageResource(R.drawable.comedy);
                break;
            case "СЕРИАЛЫ": imageView.setImageResource(R.drawable.serials);
                break;
            case "МУЛЬТФИЛЬМЫ": imageView.setImageResource(R.drawable.cartoons1);
                break;
            case "ДРАМЫ": imageView.setImageResource(R.drawable.drama);
                break;
            case "ГОЛОВОЛОМКИ": imageView.setImageResource(R.drawable.puzzle);
                break;
            case "РОССИЙСКИЙ/СССР": imageView.setImageResource(R.drawable.russia);
                break;
            case "СОЛЯНКА": imageView.setImageResource(R.drawable.saltwort_1);
                break;
            case "СУПЕР": imageView.setImageResource(R.drawable.superhero);
                break;
            case "СОЛЯНКА-2": imageView.setImageResource(R.drawable.saltwort_2);
                break;
            case "АКТЕРСКИЙ СОСТАВ": imageView.setImageResource(R.drawable.staff);
                break;
            case "КИНОГИК": imageView.setImageResource(R.drawable.geek);
                break;
            case "ТАК ГОВОРИЛ": imageView.setImageResource(R.drawable.said_so);
                break;
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
