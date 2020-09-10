package com;

import com.algorithms.ISortAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SortArray extends JPanel {
    private static final int DEFAULT_WINDOW_WIDTH = 1920;
    private static final int DEFAULT_WINDOW_HEIGHT = 1080;
    private static final int DEFAULT_BAR_WIDTH = 10;

    private static final double BAR_HEIGHT_PERCENTAGE = 384.0 / 1080.0;
    private static final int NUM_BARS = DEFAULT_WINDOW_WIDTH / DEFAULT_WINDOW_HEIGHT;

    private final int[] array;
    private final int[] barColors;
    private String algorithmName = "";
    private ISortAlgorithm algorithm;
    private long algorithmDelay = 0;

    private JSpinner spinner;

    private int arrayChanges = 0; // Number of times the current algorithm has changed the array

    public SortArray() {
        setBackground(Color.GRAY);
        array = new int[NUM_BARS];
        barColors = new int[NUM_BARS];

        for(int i = 0; i < NUM_BARS; i++) {
            array[i] = i;
            barColors[i] = 0;
        }

        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spinner.addChangeListener(event -> {
            algorithmDelay = (Integer) spinner.getValue();
            algorithm.setDelay(algorithmDelay);
        });
        add(spinner, BorderLayout.LINE_START);
    }

    public int arraySize() {
        return array.length;
    }

    public int getValue(int index) {
        return array[index];
    }

    public int getMaxValue() {
        return Arrays.stream(array).max().orElse(Integer.MIN_VALUE);
    }

    private void finaliseUpdate(long milliSecondDelay, boolean isStep) {
        repaint();
        try {
            Thread.sleep(milliSecondDelay);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if(isStep) {
            arrayChanges++;
        }
    }

    public void swap(int first, int second, boolean isStep, long milliSecondsDelay) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;

        barColors[first] = 50;
        barColors[second] = 50;

        finaliseUpdate(milliSecondsDelay, isStep);
    }

    public void updateSingle(int index, int value, long milliSecondsDelay, boolean isStep) {
        array[index] = value;
        barColors[index] = 50;

        finaliseUpdate(milliSecondsDelay, isStep);
        repaint();
    }

    public void shuffle() {
        arrayChanges = 0;
        Random random = new Random();
        for(int i = 0; i < arraySize(); i++) {
            int swapWith = random.nextInt(arraySize() - 1);
            swap(i, swapWith, false, 10);
        }
        arrayChanges = 0;
    }

    public void highlightArray() {
        for(int i = 0; i < arraySize(); i++) {
            updateSingle(i, getValue(i), 10, false);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    }

    public void resetColors() {
        for(int i = 0; i < arraySize(); i++) {
            barColors[i] = 0;
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D panelGraphics = (Graphics2D) g.create();

        try {
            Map<RenderingHints.Key, Object> renderingHints = new HashMap<>();
            renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            panelGraphics.addRenderingHints(renderingHints);
            panelGraphics.setColor(Color.PINK);
            panelGraphics.setFont(new Font("Monospaced", Font.BOLD, 20));
            panelGraphics.drawString("Algorithm Running: "+algorithmName, 10, 30);
            panelGraphics.drawString("Time Delay: "+algorithmDelay+"ms", 10, 55);
            panelGraphics.drawString("# times array has changed: "+arrayChanges, 10, 80);

            drawBars(panelGraphics);
        }
        finally {
            panelGraphics.dispose();
        }
    }

    private void drawBars(Graphics2D panelGraphics) {
        int barWidth = getWidth() / NUM_BARS;
        int bufferedImageWidth = getWidth();
        int bufferedImageHeight = getHeight();

        if(bufferedImageHeight > 0 && bufferedImageWidth > 0) {
            if(bufferedImageWidth < 256) {
                bufferedImageWidth = 256;
            }

            double maxValue = getMaxValue();
            BufferedImage bufferedImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, BufferedImage.TYPE_INT_ARGB);
            makeBufferedImageTransparent(bufferedImage);
            Graphics2D bufferedGraphics = null;

            try {
                bufferedGraphics = bufferedImage.createGraphics();

                for(int i = 0; i < NUM_BARS; i++) {
                    double currentValue = getValue(i);
                    double percentOfMax = currentValue / maxValue;
                    double heightPercentOfPanel = percentOfMax * BAR_HEIGHT_PERCENTAGE;
                    int height = (int) (heightPercentOfPanel * (double) getHeight());
                    int xBegin = i + (barWidth - 1) * i;
                    int yBegin = getHeight() - height;

                    int value = barColors[i] * 2;
                    if(value > 90) {
                        bufferedGraphics.setColor(new Color(255 - value, 255, 255 - value));
                    }
                    else {
                        bufferedGraphics.setColor(new Color(255, 255 - value, 255 - value));
                    }
                    bufferedGraphics.fillRect(xBegin, yBegin, barWidth, height);
                    if(barColors[i] > 0) {
                        barColors[i] -= 5;
                    }
                }
            }
            finally {
                if(bufferedGraphics != null) {
                    bufferedGraphics.dispose();
                }
            }

            panelGraphics.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        }
    }

    private void makeBufferedImageTransparent(BufferedImage bufferedImage) {
        Graphics2D bufferedGraphics = null;
        try {
            bufferedGraphics = bufferedImage.createGraphics();
            bufferedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
            bufferedGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            bufferedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }
        finally {
            if(bufferedGraphics != null) {
                bufferedGraphics.dispose();
            }
        }
    }

    public void setName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setAlgorithm(ISortAlgorithm algorithm) {
        this.algorithm = algorithm;
        algorithmDelay = algorithm.getDelay();
        spinner.setValue((int) algorithm.getDelay());
    }
}
