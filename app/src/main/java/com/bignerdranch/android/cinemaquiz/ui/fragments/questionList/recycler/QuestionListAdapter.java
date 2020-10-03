package com.bignerdranch.android.cinemaquiz.ui.fragments.questionList.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Question;

import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListHolder> {

    private List<Question> items;
    private Consumer<Integer> itemClick;
    private int lastQuestionId;

    public QuestionListAdapter(List<Question> items, Consumer<Integer> itemClick, int lastQuestionId) {
        this.items = items;
        this.itemClick = itemClick;
        this.lastQuestionId = lastQuestionId;
    }

    public void updateLastQuestionId(int lastQuestionId) {
        this.lastQuestionId = lastQuestionId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListHolder holder, int position) {
        holder.bind(items.get(position), itemClick, lastQuestionId);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
