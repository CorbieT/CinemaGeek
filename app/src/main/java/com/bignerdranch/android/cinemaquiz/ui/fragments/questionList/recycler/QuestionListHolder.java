package com.bignerdranch.android.cinemaquiz.ui.fragments.questionList.recycler;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;

class QuestionListHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.question_list_number)
    TextView questionNumber;

    @BindView(R.id.question_cell_container)
    RelativeLayout holderContainer;

    public QuestionListHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Question question, Consumer<Integer> itemClick, int lastQuestionId) {
        questionNumber.setText(String.valueOf(question.getId() + 1));
        if (question.getId() < lastQuestionId) {
            holderContainer.setBackgroundResource(R.drawable.play_button3);
        } else if (question.getId() > lastQuestionId) {
            holderContainer.setBackgroundResource(R.drawable.play_button);
        } else {
            holderContainer.setBackgroundResource(R.drawable.play_button2);
        }
        itemView.setOnClickListener(view -> itemClick.accept(question.getId()));
    }
}
