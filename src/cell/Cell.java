package cell;

public class Cell {

    private CellState state;
    private int aliveNeighboursCount;

    public Cell(){
        this.state = CellState.DEAD;
        this.aliveNeighboursCount = 0;
    }

    public void setAliveNeighboursCount(int aliveNeighboursCount) {
        this.aliveNeighboursCount = Math.min(aliveNeighboursCount, 8);
    }

    public CellState getNextState(){
        boolean isAlive = state == CellState.ALIVE;
        if (isAlive && (this.aliveNeighboursCount < 2 || this.aliveNeighboursCount > 3)){
            return CellState.DEAD;
        } else if (!isAlive && this.aliveNeighboursCount == 3){
            return CellState.ALIVE;
        }
        return state;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }
}
