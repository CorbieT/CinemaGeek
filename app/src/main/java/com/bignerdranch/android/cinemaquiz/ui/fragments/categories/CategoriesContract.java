package com.bignerdranch.android.cinemaquiz.ui.fragments.categories;

import com.bignerdranch.android.cinemaquiz.ui.fragments.BaseViewLoaderContract;

import java.util.List;

public interface CategoriesContract {
    interface View<T> extends BaseViewLoaderContract {
        void setupRecycler(List<T> newItems);
        void showErrorLoadingCategories(String msg);
    }

    interface Presenter {
    }
}
