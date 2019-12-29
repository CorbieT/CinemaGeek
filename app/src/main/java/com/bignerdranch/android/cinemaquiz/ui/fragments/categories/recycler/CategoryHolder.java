package com.bignerdranch.android.cinemaquiz.ui.fragments.categories.recycler;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Category;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_categories_title_text_view)
    TextView titleTextView;

    @BindView(R.id.item_container)
    FrameLayout itemContainer;

    public CategoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Category category, Consumer<String> itemClick) {
        titleTextView.setText(category.getTitle());

        itemContainer.setBackgroundResource(category.isCompleted() ?
                R.drawable.category_list_complete : R.drawable.category_list);

        itemView.setOnClickListener(view -> itemClick.accept(category.getTitle()));
    }


}
