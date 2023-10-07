package org.olisarczi.game;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public class Game {
    private final Board board;
    private Grid grid;
    private GameState gameState = GameState.STOPPED;
    private Timer timer;
    private final JButton startButton;
    private final JButton stopButton;

    private int boardWidthInTiles;
    private int boardHeightInTiles;

    @Getter
    private GameState state;

    private int timerDelay = 200;

    private final JLabel generationLabel;
    private final JLabel delayLabel;

    private Color currentThemeGridColor = Color.BLACK;

    public Game(int pixelsWidth, int pixelsHeight, int tileSize) {
        this.boardWidthInTiles = pixelsWidth / tileSize;
        this.boardHeightInTiles = pixelsHeight / tileSize;
        this.generationLabel = new JLabel("Generation: 0");
        JFrame frame = new JFrame("Game of Life");
        board = new Board(boardWidthInTiles, boardHeightInTiles, tileSize);

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        JButton resetButton = new JButton("Reset");
        delayLabel = new JLabel("Delay (ms): " + timerDelay);
        JButton increaseDelayButton = new JButton("+");
        JButton decreaseDelayButton = new JButton("-");
        JToggleButton toggleButton = new JToggleButton("Grid ON");
        JLabel themeLabel = new JLabel("Theme: ");
        String[] options = {"Vanilla", "Fire", "Blueprint"};
        JComboBox<String> comboBox = new JComboBox<>(options);

        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("Grid OFF");
                board.setGridColor(board.getBackground());
                board.repaint();
            } else {
                toggleButton.setText("Grid ON");
                board.setGridColor(currentThemeGridColor);
                board.repaint();
            }
        });

        comboBox.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            switch (Objects.requireNonNull(selectedOption)) {
                case "Vanilla" -> {
                    board.setBackground(Color.GRAY);
                    board.setAliveCellColor(Color.WHITE);
                    if (toggleButton.isSelected()) {
                        board.setGridColor(Color.GRAY);
                    } else {
                        board.setGridColor(Color.BLACK);
                    }
                    currentThemeGridColor = Color.BLACK;
                    board.repaint();
                }
                case "Fire" -> {
                    board.setBackground(Color.BLACK);
                    board.setAliveCellColor(Color.ORANGE);
                    if (toggleButton.isSelected()) {
                        board.setGridColor(Color.BLACK);
                    } else {
                        board.setGridColor(Color.GRAY);
                    }
                    currentThemeGridColor = Color.GRAY;
                    board.repaint();
                }
                case "Blueprint" -> {
                    Color darkBlue = new Color(0, 32, 128);
                    board.setBackground(darkBlue);
                    board.setAliveCellColor(Color.YELLOW);
                    if (toggleButton.isSelected()) {
                        board.setGridColor(darkBlue);
                    } else {
                        board.setGridColor(Color.DARK_GRAY);
                    }
                    currentThemeGridColor = Color.DARK_GRAY;
                    board.repaint();
                }
            }
        });

        startButton.addActionListener(e -> {
            if (gameState == GameState.STOPPED) {
                startGame();
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.RUNNING;
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                resumeGame();
            }
        });

        stopButton.addActionListener(e -> {
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                pauseGame();
            }
        });

        resetButton.addActionListener(e -> {
            if (gameState == GameState.RUNNING) {
                stopGame();
            } else {
                gameState = GameState.STOPPED;
                board.clearBoard();
            }
        });

        increaseDelayButton.addActionListener(e -> {
            timerDelay += 10;
            delayLabel.setText("Delay (ms): " + timerDelay);
            timer.setDelay(timerDelay);
        });

        decreaseDelayButton.addActionListener(e -> {
            if (timerDelay >= 20) {
                timerDelay -= 10;
                delayLabel.setText("Delay (ms): " + timerDelay);
                timer.setDelay(timerDelay);
            }
        });

        frame.add(board, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generationLabel);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(delayLabel);
        buttonPanel.add(increaseDelayButton);
        buttonPanel.add(decreaseDelayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        JPanel upperPanel = new JPanel();
        upperPanel.add(themeLabel);
        upperPanel.add(comboBox);
        upperPanel.add(toggleButton);
        frame.add(upperPanel, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Update the board with the new dimensions in pixels
                boardWidthInTiles = frame.getWidth() / tileSize;
                boardHeightInTiles= frame.getHeight() / tileSize;

                board.setBoardWidthInTiles(boardWidthInTiles);
                board.setBoardHeightInTiles(boardHeightInTiles);

                // Update the board with the new dimensions in tiles
                board.updateBoardSize();
                board.repaint();

                grid = new Grid(boardWidthInTiles, boardHeightInTiles);
                setUserAliveCells();
            }
        });

        frame.setVisible(true);
    }

    private void startGame() {
        if (gameState == GameState.PAUSED) {
            resumeGame();
        } else {
            grid = new Grid(boardWidthInTiles, boardHeightInTiles);
            setUserAliveCells();
        }
        gameState = GameState.RUNNING;
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        final int[] generation = {1};
        timer = new Timer(timerDelay, e -> {
            if (gameState == GameState.RUNNING) {
                grid = grid.getNextGeneration();
                List<Point> nextAliveCellsCords = grid.getAliveCellsCords();

                System.out.println("Alive cells in generation " + generation[0] + ":");
                System.out.println("Generation " + generation[0] + ":");
                System.out.println(nextAliveCellsCords);
                generationLabel.setText("Generation: " + generation[0]);

                board.setSelectedCoordinates(nextAliveCellsCords);
                if (nextAliveCellsCords.isEmpty()) {
                    stopGame();
                    sendMessage("Simulation stopped. All cells are dead. It took " + generation[0] + " generations.");
                }
                generation[0]++;
            }
        });
        timer.start();
    }

    private void pauseGame() {
        gameState = GameState.PAUSED;
        timer.stop();
    }

    private void resumeGame() {
        gameState = GameState.RUNNING;
        setUserAliveCells();
        timer.start();
    }

    private void stopGame() {
        gameState = GameState.STOPPED;
        timer.stop();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        board.clearBoard();
    }

    private void sendMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void setUserAliveCells() {
        List<Point> initialAliveCells = board.getSelectedCoordinates();
        checkUserInput(initialAliveCells, boardWidthInTiles, boardHeightInTiles);
        grid.setCellsAlive(initialAliveCells);
    }

    private void checkUserInput(List<Point> userCells, int maxWidth, int maxHeight) {
        for (int index = 0; index < userCells.size(); index++) {
            Point tempCoordinates = userCells.get(index);
            if (tempCoordinates.x < 0) tempCoordinates.x = 0;
            if (tempCoordinates.x >= maxWidth) tempCoordinates.x = maxWidth - 1;
            if (tempCoordinates.y < 0) tempCoordinates.y = 0;
            if (tempCoordinates.y >= maxHeight) tempCoordinates.y = maxHeight - 1;
            userCells.set(index, tempCoordinates);
        }
    }
}
