package com.model;

import java.util.ArrayList;
import java.util.List;

public class CellContainer {
    private final int width;
    private final int height;

    private List<List<Cell>> cells;

    public CellContainer(int width, int height){
        this.width = width;
        this.height = height;
        this.cells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            List<Cell> row = new ArrayList<>();
            for (int y = 0; y < height; y++) {
                row.add(new Cell());
            }
            cells.add(row);
        }
    }

    public Cell getCell(int x, int y){
        return cells.get(x).get(y);
    }
}
