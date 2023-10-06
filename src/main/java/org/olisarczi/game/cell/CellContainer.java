package org.olisarczi.game.cell;

import java.util.ArrayList;
import java.util.List;

public class CellContainer {

    private final List<List<Cell>> cells;

    public CellContainer(int width, int height){
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
