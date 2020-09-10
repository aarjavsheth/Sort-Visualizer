package com;

import com.screens.MainMenuScreen;
import com.screens.Screen;

import javax.swing.*;
import java.util.ArrayList;

public class SortVisualizer {
    private final JFrame window;

    public static final int WINDOWWIDTH = 1024;
    public static final int WINDOWHEIGHT = 768;

    private final ArrayList<Screen> screens;

    public SortVisualizer() {
        screens = new ArrayList<>();
        window = new JFrame("Sorting Visualizer");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public Screen getCurrentScreen() {
        return screens.get(screens.size() - 1);
    }

    public void pushScreen(Screen screen) {
        if(!screens.isEmpty()) {
            window.remove(getCurrentScreen());
        }
        screens.add(screen);
        window.setContentPane(screen);
        window.validate();
        screen.onOpen();
    }

    public void popScreen() {
        if(!screens.isEmpty()) {
            Screen prev = getCurrentScreen();
            screens.remove(prev);
            window.remove(prev);
            if(!screens.isEmpty()) {
                Screen current = getCurrentScreen();
                window.setContentPane(current);
                window.validate();
                current.onOpen();
            }
            else {
                window.dispose();
            }
        }
    }

    private void start() {
        pushScreen(new MainMenuScreen(this));
        window.pack();
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(() -> {
            new SortVisualizer().start();
        });
    }
}
