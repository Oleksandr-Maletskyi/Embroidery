package ua.university;

import javax.swing.*;

public class EmbroideryApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmbroideryModel model = new EmbroideryModel(50, 50);
            EmbroideryView view = new EmbroideryView(model);
            new EmbroideryController(model, view);
            view.setVisible(true);
        });
    }
}