package com.bignerdranch.android.cinemaquiz.ui.fragments.question.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.ui.fragments.question.QuestionFragment;

import java.util.List;

class QuestionFragmentStateAdapter extends FragmentStateAdapter {

    private List<Question> items;
    private String category;

    public QuestionFragmentStateAdapter(@NonNull Fragment fragment, List<Question> items, String category) {
        super(fragment);
        this.items = items;
        this.category = category;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return QuestionFragment.newInstance(category, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
