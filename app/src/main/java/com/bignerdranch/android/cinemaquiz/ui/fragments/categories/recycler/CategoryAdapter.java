package com.bignerdranch.android.cinemaquiz.ui.fragments.categories.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Category;

import java.util.List;

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
        ((CategoryHolder) holder).bind(items.get(position), itemClick);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
