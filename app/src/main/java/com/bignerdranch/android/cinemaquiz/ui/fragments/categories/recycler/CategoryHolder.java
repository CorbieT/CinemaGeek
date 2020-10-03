package com.bignerdranch.android.cinemaquiz.ui.fragments.categories.recycler;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Category;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_categories_title_text_view)
    TextView titleTextView;

    @BindView(R.id.item_container)
    FrameLayout itemContainer;

    @BindView(R.id.list_categories_image_view)
    ImageView completeImageView;

    public CategoryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Category category, Consumer<String> itemClick, int position) {
        titleTextView.setText(category.getTitle());

        completeImageView.setVisibility(category.isCompleted() ?
                View.VISIBLE : View.GONE);

        if (position % 2 == 0) itemContainer.setBackgroundResource(R.drawable.category_list);
        else itemContainer.setBackgroundResource(R.drawable.category_list2);

        itemView.setOnClickListener(view -> itemClick.accept(category.getTitle()));
    }


}
