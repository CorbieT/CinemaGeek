package com.bignerdranch.android.cinemaquiz.repositories.questions;

import com.bignerdranch.android.cinemaquiz.repositories.Repository;

import java.util.List;

import io.reactivex.Single;

public interface QuestionRepository<T> extends Repository {
    Single<List<T>> parseQuestions();
}
