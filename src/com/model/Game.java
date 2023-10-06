package com.model;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Game {
    private final Board board;
    private Grid grid;
    private boolean simulationStarted = false;
    private Timer timer;
    private final JButton startButton;
    private final JButton resetButton;

    private int boardWidth;
    private int boardHeight;

    public Game(int width, int height) {
        this.boardWidth = width;
        this.boardHeight = height;
        JFrame frame = new JFrame("Game of Life");
        board = new Board(boardWidth, boardHeight);

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");

        startButton.addActionListener(e -> {
            if (!simulationStarted) {
                startGame(boardWidth, boardHeight);
            }
        });

        resetButton.addActionListener(e -> {
            if (simulationStarted) {
                stopGame();
            }
        });

        frame.add(board, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(resetButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Get the new width and height of the board
                boardWidth = frame.getWidth();
                boardHeight = frame.getHeight();

                // Update the board with the new dimensions
                board.updateBoardSize(boardWidth, boardHeight);
            }
        });

        frame.setVisible(true);
    }

    private void startGame(int width, int height) {
        grid = new Grid(new int[]{width, height});
        List<List<Integer>> initialAliveCells = board.getSelectedCoordinates();
        checkUserInput(initialAliveCells, width, height);
        grid.setCellsAlive(initialAliveCells);
        simulationStarted = true;
        startButton.setEnabled(false);
        resetButton.setEnabled(true);
        final int[] generation = {1};
        timer = new Timer(200, e -> {
            List<List<Integer>> nextGeneration = grid.createGeneration();
            System.out.println("Alive cells in generation " + generation[0] + ":");
            System.out.println(nextGeneration);
            board.setSelectedCoordinates(nextGeneration);
            System.out.println("Generation " + generation[0] + ":");
            System.out.println(nextGeneration);
            if (nextGeneration.isEmpty()) {
                stopGame();
                sendMessage("Simulation stopped. All cells are dead. It took " + generation[0] + " generations.");
            }
            generation[0]++;
        });
        timer.start();
    }

    private void stopGame() {
        simulationStarted = false;
        timer.stop();
        startButton.setEnabled(true); // Enable Start button
        resetButton.setEnabled(false); // Disable Reset button
        board.clearBoard();
    }

    private void sendMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void checkUserInput(List<List<Integer>> userCells, int maxWidth, int maxHeight) {
        for (int index = 0; index < userCells.size(); index++) {
            List<Integer> tempCoordinates = userCells.get(index);
            if (tempCoordinates.get(0) < 0) tempCoordinates.set(0, 0);
            if (tempCoordinates.get(0) >= maxWidth) tempCoordinates.set(0, maxWidth - 1);
            if (tempCoordinates.get(1) < 0) tempCoordinates.set(1, 0);
            if (tempCoordinates.get(1) >= maxHeight) tempCoordinates.set(1, maxHeight - 1);
            userCells.set(index, tempCoordinates);
        }
    }
}
