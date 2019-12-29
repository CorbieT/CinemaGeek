package com.bignerdranch.android.cinemaquiz.ui.fragments.categories;

import android.annotation.SuppressLint;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

import com.bignerdranch.android.cinemaquiz.common.BasePresenter;
import com.bignerdranch.android.cinemaquiz.model.Category;
import com.bignerdranch.android.cinemaquiz.repositories.categories.CategoryRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CategoriesPresenter extends BasePresenter implements CategoriesContract.Presenter, LifecycleObserver {

    private CategoriesContract.View<Category> view;
    private CategoryRepository<Category> repository;

    public CategoriesPresenter(CategoriesContract.View<Category> view,
                               CategoryRepository<Category> repository,
                               Lifecycle lifecycle) {
        this.view = view;
        this.repository = repository;
        lifecycle.addObserver(this);
        loadCategories();
    }

    @SuppressLint("CheckResult")
    private void loadCategories() {
        disposables.add(repository.parseCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> view.showLoader())
                .doFinally(() -> view.hideLoader())
                .subscribe(response -> view.setupRecycler(response),
                        error -> view.showErrorLoadingCategories("списка категорий")));
    }
}
