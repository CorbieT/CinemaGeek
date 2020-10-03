package com.bignerdranch.android.cinemaquiz.repositories.questions;

import com.bignerdranch.android.cinemaquiz.model.Question;
import com.bignerdranch.android.cinemaquiz.repositories.Repository;

import java.util.List;

import io.reactivex.Single;

public interface QuestionRepository extends Repository {
    Single<List<Question>> parseQuestions();
}
