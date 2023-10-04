package com.model;

public class Cell {
    public Cell(){
        this.isAlive = false;
        this.aliveNeighbours = 0;
    }

    private boolean isAlive;
    private int aliveNeighbours;

    public boolean isAlive() {
        return isAlive;
    }

    public void makeCellAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    public void setAliveNeighbours(int aliveNeighbours) {
        this.aliveNeighbours = Math.min(aliveNeighbours, 8);
    }

    public void setCellState(){
        if (this.isAlive && (this.aliveNeighbours < 2 || this.aliveNeighbours > 3)){
            this.isAlive = false;
        } else if (!this.isAlive && this.aliveNeighbours == 3){
            this.isAlive = true;
        }
    }

}
