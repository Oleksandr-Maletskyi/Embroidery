package ua.university;

import javax.swing.*;
import java.awt.*;

class CanvasPanel extends JPanel {
    private final EmbroideryModel model;
    private int cellSize = 12;

    public CanvasPanel(EmbroideryModel model) {
        this.model = model;
        updateSize();
        this.setBackground(new Color(245, 245, 240));
    }

    public void updateSize() {
        int width = model.getCols() * cellSize;
        int height = model.getRows() * cellSize;
        this.setPreferredSize(new Dimension(width, height));
        this.revalidate();
    }

    public void setCellSize(int size) {
        this.cellSize = size;
        updateSize();
        repaint();
    }

    public int getCellSize() {
        return cellSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Rectangle clip = g.getClipBounds();

        int startCol = 0, startRow = 0;
        int endCol = model.getCols(), endRow = model.getRows();
        //Рахує і відмальовує тільки клітинки як влазять в поле зору
        if (clip != null) {
            startCol = Math.max(0, clip.x / cellSize);
            startRow = Math.max(0, clip.y / cellSize);
            endCol = Math.min(model.getCols(), (clip.x + clip.width) / cellSize + 1);
            endRow = Math.min(model.getRows(), (clip.y + clip.height) / cellSize + 1);
        }

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                Color c = model.getPixel(row, col);
                if (!c.equals(Color.WHITE)) {
                    g.setColor(c);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }
        }
        //Малює лінії
        g.setColor(new Color(220, 220, 220));
        for (int row = startRow; row <= endRow; row++) {
            int lineY = row * cellSize;
            int startX = (clip != null) ? clip.x : 0;
            int endX = (clip != null) ? clip.x + clip.width : model.getCols() * cellSize;
            g.drawLine(startX, lineY, endX, lineY);
        }
        for (int col = startCol; col <= endCol; col++) {
            int lineX = col * cellSize;
            int startY = (clip != null) ? clip.y : 0;
            int endY = (clip != null) ? clip.y + clip.height : model.getRows() * cellSize;
            g.drawLine(lineX, startY, lineX, endY);
        }
    }
}
