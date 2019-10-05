package com.bignerdranch.android.cinemaquiz.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomHelper {
    private final List<Integer> numbers;
    private final int bound;
    private int index = 0;

    public RandomHelper(int bound) {
        this.bound = bound;
        this.numbers = new ArrayList<>(bound);
        for (int i = 0; i < bound; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
    }

    public int nextUniqueInt() {
        int returnValue = numbers.get(index);
        if (index < bound) index++;
        else throw new IndexOutOfBoundsException("Index out of range");
        return returnValue;
    }
}
