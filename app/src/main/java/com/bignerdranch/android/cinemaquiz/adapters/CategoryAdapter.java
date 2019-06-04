package com.bignerdranch.android.cinemaquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.common.HawkManager;
import com.bignerdranch.android.cinemaquiz.model.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Category> items;
    private Consumer<String> itemClick;

    public CategoryAdapter(Context context, List<Category> items, Consumer<String> itemClick) {
        this.context = context;
        this.items = items;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(context).inflate(R.layout.list_categories_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Category category = items.get(position);
        ((CategoryHolder) holder).mTitleTextView.setText(category.getTitle());

        if (HawkManager.getInstance().isCategoryComplete(category.getTitle())) ((CategoryHolder) holder).mFrameLayout.setBackgroundResource(R.drawable.category_list_complete);
        else ((CategoryHolder) holder).mFrameLayout.setBackgroundResource(R.drawable.category_list);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.list_categories_title_text_view)
        TextView mTitleTextView;

        @BindView(R.id.list_container)
        FrameLayout mFrameLayout;

        @Override
        public void onClick(View view) {
            itemClick.accept(mTitleTextView.getText().toString());
        }

        private CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
    }

}
