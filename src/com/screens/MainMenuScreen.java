package com.screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.SortVisualizer;
import com.algorithms.*;

public final class MainMenuScreen extends Screen {
    private static final Color BACKGROUND = Color.GRAY;
    private final ArrayList<AlgorithmCheckBox> checkBoxes;

    public MainMenuScreen(SortVisualizer app) {
        super(app);
        checkBoxes = new ArrayList<>();
        setGUI();
    }

    private void addCheckBox(ISortAlgorithm algorithm, JPanel panel) {
        JCheckBox box = new JCheckBox("", true);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setBackground(BACKGROUND);
        box.setForeground(Color.WHITE);
        checkBoxes.add(new AlgorithmCheckBox(algorithm, box));
        panel.add(box);
    }

    private void initContainer(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(BACKGROUND);
    }

    public void setGUI() {
        JPanel sortAlgorithmContainer =  new JPanel();
        JPanel optionsContainer = new JPanel();
        JPanel outerContainer = new JPanel();
        initContainer(this);
        initContainer(optionsContainer);
        initContainer(sortAlgorithmContainer);

        outerContainer.setBackground(BACKGROUND);
        outerContainer.setLayout(new BoxLayout(outerContainer, BoxLayout.LINE_AXIS));

//        try {
//            ClassLoader loader = getClass().getClassLoader();
//            BufferedImage image = ImageIO.read(new File(loader.getResource("logo.png").getFile()));
//            JLabel label = new JLabel(new ImageIcon(image));
//            label.setAlignmentX(Component.CENTER_ALIGNMENT);
//            add(label);
//        }
//        catch(IOException e) {
//            System.out.println("Error loading logo");
//        }

        sortAlgorithmContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        addCheckBox(new BubbleSort(), sortAlgorithmContainer);
        addCheckBox(new InsertionSort(), sortAlgorithmContainer);
//        addCheckBox(new MergeSort(), sortAlgorithmContainer);
//        addCheckBox(new SelectionSort(), sortAlgorithmContainer);

        // TODO: Add Music

        JButton sortButton = new JButton("Sort Array");
        sortButton.addActionListener((ActionEvent e) -> {
            ArrayList<ISortAlgorithm> algorithms =new ArrayList<ISortAlgorithm>();
            for(AlgorithmCheckBox cb : checkBoxes) {
                if(cb.isSelected()) {
                    algorithms.add(cb.getAlgorithm());
                }
            }

            app.pushScreen(new SortingScreen(algorithms, app));
        });
        sortButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        outerContainer.add(optionsContainer);
        outerContainer.add(Box.createRigidArea(new Dimension(5,0)));
        outerContainer.add(sortAlgorithmContainer);
    }

    @Override
    public void onOpen() {
        checkBoxes.forEach(box -> {
            box.unselect();
        });
    }

    private class AlgorithmCheckBox {
        private final ISortAlgorithm algorithm;
        private final JCheckBox box;

        public AlgorithmCheckBox(ISortAlgorithm algorithm, JCheckBox box) {
            this.algorithm = algorithm;
            this.box = box;
            this.box.setText(algorithm.getName());
        }

        public void unselect() {
            box.setSelected(false);
        }

        public boolean isSelected() {
            return box.isSelected();
        }

        public ISortAlgorithm getAlgorithm() {
            return algorithm;
        }
    }
}
