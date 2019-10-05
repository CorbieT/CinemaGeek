package com.bignerdranch.android.cinemaquiz.interfaces;

import java.util.List;

public interface Repository<T> {
    List<T> query(Specification specification);
}
