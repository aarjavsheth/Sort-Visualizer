package com.algorithms;

import com.SortArray;

public class BubbleSort implements ISortAlgorithm {

    private long stepDelay = 2;
    @Override
    public String getName() {
        return "Bubble Sort";
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
            for(int j = 0; j < sortArray.arraySize() - i - 1; j++) {
                if(sortArray.getValue(i) > sortArray.getValue(j + 1)) {
                    sortArray.swap(i, j + 1,true, getDelay());
                }
            }
        }
    }
}
