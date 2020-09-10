package com.algorithms;

import com.SortArray;

public class InsertionSort implements ISortAlgorithm {

    private long stepDelay = 1;
    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public long getDelay() {
        return stepDelay;
    }

    @Override
    public void setDelay(long delay) {
        this.stepDelay = delay;
    }

    @Override
    public void runSort(SortArray sortArray) {
        for(int i = 0; i < sortArray.arraySize(); i++) {
            int key = sortArray.getValue(i);
            int j = i - 1;
            while(j >= 0 && sortArray.getValue(j) > key) {
                sortArray.updateSingle(j + 1, sortArray.getValue(j), stepDelay, true);
            }
            sortArray.updateSingle(j + 1, key, getDelay(), true);
        }
    }
}
