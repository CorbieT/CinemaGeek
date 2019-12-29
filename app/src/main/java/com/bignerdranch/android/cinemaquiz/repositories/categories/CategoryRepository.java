package com.bignerdranch.android.cinemaquiz.repositories.categories;

import com.bignerdranch.android.cinemaquiz.repositories.Repository;

import java.util.List;

import io.reactivex.Single;

public interface CategoryRepository<T> extends Repository {
    Single<List<T>> parseCategories();
}
