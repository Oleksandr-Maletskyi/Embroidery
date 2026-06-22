package ua.university;

import java.awt.*;

class EmbroideryModel {
    private int rows;
    private int cols;
    private Color[][] grid;
    private Color currentColor = new Color(200, 0, 0);

    public EmbroideryModel(int rows, int cols) {
        initGrid(rows, cols);
    }

    private void initGrid(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.grid = new Color[rows][cols];
        clear();
    }

    public void clear() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Color.WHITE;
            }
        }
    }

    public void resetSize(int r, int c) {
        initGrid(r, c);
    }

    public void duplicateHorizontal() {
        int newCols = cols * 2;
        Color[][] newGrid = new Color[rows][newCols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = grid[r][c];
                newGrid[r][c + cols] = grid[r][c];
            }
        }
        this.cols = newCols;
        this.grid = newGrid;
    }

    public void duplicateVertical() {
        int newRows = rows * 2;
        Color[][] newGrid = new Color[newRows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                newGrid[r][c] = grid[r][c];
                newGrid[r + rows][c] = grid[r][c];
            }
        }
        this.rows = newRows;
        this.grid = newGrid;
    }

    public void setPixel(int row, int col, Color color) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            grid[row][col] = color;
        }
    }

    public Color getPixel(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grid[row][col];
        }
        return Color.WHITE;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }
}
