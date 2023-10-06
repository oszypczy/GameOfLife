package org.olisarczi.game;

import org.olisarczi.game.cell.CellContainer;
import org.olisarczi.game.cell.CellState;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int width;
    private final int height;

    private final CellContainer cells;

    public Grid(int width, int height){
        this.width = width;
        this.height = height;
        this.cells = new CellContainer(width, height);
    }

    public Grid getNextGeneration() {
        Grid nextGeneration = new Grid(width, height);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                CellState nextState = cells.getCell(x, y).getNextState();
                nextGeneration.setCellState(x, y, nextState);
            }
        }
        nextGeneration.refreshAllCellsNeighboursCount();
        return nextGeneration;
    }

    public void setCellsAlive(List<Point> aliveCellsCords) {
        for (Point aliveCellCords : aliveCellsCords){
            int x = aliveCellCords.x;
            int y = aliveCellCords.y;
            setCellState(x, y, CellState.ALIVE);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.getCell(x, y).setAliveNeighboursCount(getCellAliveNeighbours(x, y));
            }
        }
    }

    public List<Point> getAliveCellsCords() {
        List<Point> aliveCellsCords = new ArrayList<>();
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                CellState isCellAlive = cells.getCell(x, y).getState();
                if (isCellAlive == CellState.ALIVE){
                    aliveCellsCords.add(new Point(x, y));
                }
            }
        }
        return aliveCellsCords;
    }

    private void refreshAllCellsNeighboursCount() {
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                cells.getCell(x, y).setAliveNeighboursCount(getCellAliveNeighbours(x, y));
            }
        }
    }

    public void setCellState(int x, int y, CellState state){
        cells.getCell(x, y).setState(state);
    }

    private int getCellAliveNeighbours(int i, int j) {
        int aliveNeighbours = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k >= 0 && k < width && l >= 0 && l < height) {
                    if (k != i || l != j){
                        CellState state = cells.getCell(k, l).getState();
                        if (state == CellState.ALIVE) {
                            aliveNeighbours++;
                        }
                    }
                }
            }
        }
        return aliveNeighbours;
    }
}
