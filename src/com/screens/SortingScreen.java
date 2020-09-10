package com.screens;

import com.SortArray;
import com.SortVisualizer;
import com.algorithms.ISortAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SortingScreen extends Screen {
    private final SortArray sortArray;
    private final ArrayList<ISortAlgorithm> sortQueue;

    public SortingScreen(ArrayList<ISortAlgorithm> algorithms, SortVisualizer app) {
        super(app);
        setLayout(new BorderLayout());
        sortArray = new SortArray();
        add(sortArray, BorderLayout.CENTER);
        sortQueue = algorithms;
    }

    private void longSleep() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void shuffleAndWait() {
        sortArray.shuffle();
        sortArray.resetColors();
        longSleep();
    }

    @Override
    public void onOpen() {
        // Running on a worker thread else it would block the EventDispatchThread.
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Thread.sleep(250);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for(ISortAlgorithm algorithm : sortQueue) {
                    shuffleAndWait();

                    sortArray.setName(algorithm.getName());
                    sortArray.setAlgorithm(algorithm);

                    algorithm.runSort(sortArray);
                    sortArray.resetColors();
                    sortArray.highlightArray();
                    sortArray.resetColors();
                    longSleep();
                }
                return null;
            }

            @Override
            public void done() {
                app.popScreen();
            }
        };

        worker.execute();
    }
}
