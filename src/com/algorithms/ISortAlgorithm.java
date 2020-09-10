package com.algorithms;

import com.SortArray;

public interface ISortAlgorithm {
    public String getName();
    public long getDelay();
    public void setDelay(long delay);
    public void runSort(SortArray sortArray);
}
