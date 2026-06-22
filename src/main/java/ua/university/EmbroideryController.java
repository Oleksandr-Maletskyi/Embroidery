package ua.university;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class EmbroideryController {
    private final EmbroideryModel model;
    private final EmbroideryView view;
    private final int startSize = 50;

    public EmbroideryController(EmbroideryModel model, EmbroideryView view) {
        this.model = model;
        this.view = view;
        initListeners();

        generateMatrixPattern();
        SwingUtilities.invokeLater(() -> centerViewOnPattern());
    }

    private void initListeners() {
        view.colorPicker.addActionListener(e -> {
            int index = view.colorPicker.getSelectedIndex();
            switch (index) {
                case 0 -> model.setCurrentColor(new Color(200, 0, 0));
                case 1 -> model.setCurrentColor(Color.BLACK);
                case 2 -> model.setCurrentColor(new Color(0, 0, 150));
                case 3 -> model.setCurrentColor(new Color(0, 100, 0));
                case 4 -> model.setCurrentColor(Color.WHITE);
            }
        });

        MouseAdapter painter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                paintPixel(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                paintPixel(e);
            }
        };
        view.getCanvas().addMouseListener(painter);
        view.getCanvas().addMouseMotionListener(painter);

        view.btnZoomIn.addActionListener(e -> zoom(3));
        view.btnZoomOut.addActionListener(e -> zoom(-3));
        view.btnCenter.addActionListener(e -> centerViewOnPattern());

        view.btnDupHoriz.addActionListener(e -> {
            model.duplicateHorizontal();
            view.getCanvas().updateSize();
            view.getCanvas().repaint();
        });
        view.btnDupVert.addActionListener(e -> {
            model.duplicateVertical();
            view.getCanvas().updateSize();
            view.getCanvas().repaint();
        });

        view.btnClear.addActionListener(e -> {
            model.resetSize(startSize, startSize);
            centerViewOnPattern();
            view.getCanvas().updateSize();
            view.getCanvas().repaint();
        });

        view.btnSave.addActionListener(e -> saveToPNG());
        view.btnLoad.addActionListener(e -> loadFromPNG());
    }

    private void paintPixel(MouseEvent e) {
        int cellSize = view.getCanvas().getCellSize();
        int col = e.getX() / cellSize;
        int row = e.getY() / cellSize;

        if (row >= 0 && row < model.getRows() && col >= 0 && col < model.getCols()) {
            Color c = model.getCurrentColor();
            applyDraw(row, col, c);
            view.getCanvas().repaint();
        }
    }

    private void applyDraw(int row, int col, Color c) {
        boolean hSym = view.horizontalSym.isSelected();
        boolean vSym = view.verticalSym.isSelected();
        int maxR = model.getRows() - 1;
        int maxC = model.getCols() - 1;

        model.setPixel(row, col, c);
        if (hSym) model.setPixel(row, maxC - col, c);
        if (vSym) model.setPixel(maxR - row, col, c);
        if (hSym && vSym) model.setPixel(maxR - row, maxC - col, c);
    }

    private void zoom(int delta) {
        int oldSize = view.getCanvas().getCellSize();
        int newSize = Math.max(2, Math.min(60, oldSize + delta));
        if (oldSize == newSize) return;

        JViewport viewport = view.scrollPane.getViewport();
        Point pos = viewport.getViewPosition();

        view.getCanvas().setCellSize(newSize);
        view.scrollPane.validate();

        SwingUtilities.invokeLater(() -> {
            double scale = (double) newSize / oldSize;
            int newX = (int) (pos.x * scale);
            int newY = (int) (pos.y * scale);
            viewport.setViewPosition(new Point(newX, newY));
        });
    }

    private void centerViewOnPattern() {
        JViewport viewport = view.scrollPane.getViewport();
        int cellSize = view.getCanvas().getCellSize();

        int totalWidth = model.getCols() * cellSize;
        int totalHeight = model.getRows() * cellSize;

        int viewX = (totalWidth - viewport.getWidth()) / 2;
        int viewY = (totalHeight - viewport.getHeight()) / 2;

        viewport.setViewPosition(new Point(Math.max(0, viewX), Math.max(0, viewY)));
    }

    private void saveToPNG() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }
            try {
                int width = view.getCanvas().getPreferredSize().width;
                int height = view.getCanvas().getPreferredSize().height;
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();
                view.getCanvas().paintComponent(g2d);
                g2d.dispose();
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(view, "Збережено успішно!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view, "Помилка збереження: " + ex.getMessage());
            }
        }
    }

    private void loadFromPNG() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                int cellSize = view.getCanvas().getCellSize();

                int loadCols = image.getWidth() / cellSize;
                int loadRows = image.getHeight() / cellSize;

                model.resetSize(loadRows, loadCols);

                for (int r = 0; r < loadRows; r++) {
                    for (int c = 0; c < loadCols; c++) {
                        int imgX = c * cellSize + cellSize / 2;
                        int imgY = r * cellSize + cellSize / 2;
                        model.setPixel(r, c, new Color(image.getRGB(imgX, imgY)));
                    }
                }
                view.getCanvas().updateSize();
                view.getCanvas().repaint();
                centerViewOnPattern();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Помилка завантаження. Перевірте формат.");
            }
        }
    }

    private void generateMatrixPattern() {
        String[] quad = {
                "RRRRRRRRRRBRB",
                "RWWRWWWWWBWWB",
                "RWWRWWWWWWBBB",
                "RRRBBWWWWWWWW",
                "RWWBBWWWWWWWW",
                "RWWWWRWWWWWWW",
                "RWWWWWRWWWWWW",
                "RWWWWWWRWWWWW",
                "RWWWWWWWBBWWW",
                "RBWWWWWWBBWWW",
                "BWBWWWWWWWRWW",
                "RWBWWWWWWWWRW",
                "BBBWWWWWWWWWW"
        };

        Color red = new Color(200, 0, 0);
        Color black = Color.BLACK;

        int startRow = (model.getRows() - 26) / 2;
        int startCol = (model.getCols() - 26) / 2;

        for (int r = 0; r < 26; r++) {
            for (int c = 0; c < 26; c++) {
                int y = (r < 13) ? 12 - r : r - 13;
                int x = (c < 13) ? 12 - c : c - 13;

                char ch = quad[y].charAt(x);
                if (ch == 'R') {
                    model.setPixel(startRow + r, startCol + c, red);
                } else if (ch == 'B') {
                    model.setPixel(startRow + r, startCol + c, black);
                }
            }
        }
    }
}
