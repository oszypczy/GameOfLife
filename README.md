# Game of Life

## Overview

The Game of Life, also known simply as Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970. It is a zero-player game, meaning that its evolution is determined by its initial state, with no further input from humans. Despite its simple rules, the Game of Life exhibits complex and interesting patterns.

## Rules

1. **Cell State**: Each cell can be in one of two states - alive or dead.

2. **Neighborhood**: Each cell interacts with its eight neighbors, which are horizontally, vertically, or diagonally adjacent.

3. **Evolution Rules**:
    - Any live cell with fewer than two live neighbors dies (underpopulation).
    - Any live cell with two or three live neighbors survives.
    - Any live cell with more than three live neighbors dies (overpopulation).
    - Any dead cell with exactly three live neighbors becomes alive (reproduction).

## Implementation

This Java implementation of the Game of Life utilizes the Swing GUI library to provide a visual representation of the automaton's evolution.
Have fun exploring the patterns and dynamics of the Game of Life!
