package com.screens;

import static com.SortVisualizer.WINDOWWIDTH;
import static com.SortVisualizer.WINDOWHEIGHT;

import javax.swing.*;
import java.awt.*;

import com.SortVisualizer;

public abstract class Screen extends JPanel {
    protected SortVisualizer app;

    public Screen(SortVisualizer app) {
        this.app = app;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WINDOWWIDTH, WINDOWHEIGHT);
    }

    public abstract void onOpen();
}
