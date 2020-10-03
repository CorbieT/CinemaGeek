package com.bignerdranch.android.cinemaquiz.ui.fragments.categories.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.android.cinemaquiz.R;
import com.bignerdranch.android.cinemaquiz.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

    private List<Category> items;
    private Consumer<String> itemClick;

    public CategoryAdapter(List<Category> items, Consumer<String> itemClick) {
        this.items = items;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_fragment_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.bind(items.get(position), itemClick, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
