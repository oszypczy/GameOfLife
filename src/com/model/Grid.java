package com.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int width;
    private final int height;

    private CellContainer cells;

    public Grid(int width, int height){
        this.width = width;
        this.height = height;
        this.cells = new CellContainer(width, height);
    }


    public void setCellsAlive(List<Point> aliveCellsCords) {
        for (Point aliveCellCords : aliveCellsCords){
            int x = aliveCellCords.x;
            int y = aliveCellCords.y;
            setCellState(x, y, true);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.getCell(x, y).setAliveNeighboursCount(getCellAliveNeighbours(x, y));
            }
        }
    }

    public Grid getNextGeneration() {
        Grid nextGeneration = new Grid(width, height);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                boolean nextState = cells.getCell(x, y).getNextState();
                nextGeneration.setCellState(x, y, nextState);
            }
        }
        return nextGeneration;
    }

    public List<Point> getAliveCellsCords() {
        List<Point> aliveCellsCords = new ArrayList<>();
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                boolean isCellAlive = cells.getCell(x, y).getCellState();
                if (isCellAlive){
                    aliveCellsCords.add(new Point(x, y));
                }
            }
        }
        return aliveCellsCords;
    }

    public void setCellState(int x, int y, boolean isAlive){
        cells.getCell(x, y).setCellState(isAlive);
    }

    private int getCellAliveNeighbours(int i, int j) {
        int aliveNeighbours = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k >= 0 && k < width && l >= 0 && l < height) {
                    if (k != i || l != j){
                        if (cells.getCell(k, l).getCellState()) {
                            aliveNeighbours++;
                        }
                    }
                }
            }
        }
        return aliveNeighbours;
    }
}
