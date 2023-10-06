package com.model;

public class Cell {
    public Cell(){
        this.isAlive = false;
        this.aliveNeighboursCount = 0;
    }

    private boolean isAlive;
    private int aliveNeighboursCount;

    public boolean getCellState() {
        return isAlive;
    }

    public void setCellState(boolean isAlive){
        this.isAlive = isAlive;
    }

    public void setAliveNeighboursCount(int aliveNeighboursCount) {
        this.aliveNeighboursCount = Math.min(aliveNeighboursCount, 8);
    }

    public boolean getNextState(){
        if (this.isAlive && (this.aliveNeighboursCount < 2 || this.aliveNeighboursCount > 3)){
            return false;
        } else if (!this.isAlive && this.aliveNeighboursCount == 3){
            return true;
        }
        return this.isAlive;
    }

}
