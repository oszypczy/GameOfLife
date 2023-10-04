package com.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int[] dimensions;
    private final List<List<Cell>> cells = new ArrayList<>();
    public Grid(int[] dimensions){
        this.dimensions = dimensions;
        for (int i = 0; i < dimensions[0]; i++){
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < dimensions[1]; j++){
                row.add(new Cell());
            }
            cells.add(row);
        }
    }

    public void setCellsAlive(List<List<Integer>> aliveCells){
        for (List<Integer> aliveCell : aliveCells){
            int i = aliveCell.get(0);
            int j = aliveCell.get(1);
            cells.get(i).get(j).makeCellAlive(true);
        }
        for (int i = 0; i < dimensions[0]; i++){
            for (int j = 0; j < dimensions[1]; j++){
                cells.get(i).get(j).setAliveNeighbours(getCellAliveNeighbours(i, j));
            }
        }
    }

    public List<List<Integer>> createGeneration(){
        List<List<Integer>> aliveCells = new ArrayList<>();

        for (int i = 0; i < dimensions[0]; i++){
            for (int j = 0; j < dimensions[1]; j++){
                Cell cell = cells.get(i).get(j);
                cell.setAliveNeighbours(getCellAliveNeighbours(i, j));
            }
        }

        for (int i = 0; i < dimensions[0]; i++){
            for (int j = 0; j < dimensions[1]; j++){
                Cell cell = cells.get(i).get(j);
                cell.setCellState();
                if (cell.isAlive()){
                    List<Integer> aliveCell = new ArrayList<>();
                    aliveCell.add(i);
                    aliveCell.add(j);
                    aliveCells.add(aliveCell);
                }
            }
        }

        return aliveCells;
    }

    private int getCellAliveNeighbours(int i, int j){
        int aliveNeighbours = 0;
        for (int k = i - 1; k <= i + 1; k++){
            for (int l = j - 1; l <= j + 1; l++){
                if (k >= 0 && k < dimensions[0] && l >= 0 && l < dimensions[1]){
                    if (k != i || l != j){
                        if (cells.get(k).get(l).isAlive()){
                            aliveNeighbours++;
                        }
                    }
                }
            }
        }
        return aliveNeighbours;
    }
}
