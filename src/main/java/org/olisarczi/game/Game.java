package org.olisarczi.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Objects;

public class Game {
    private Board board;
    private Grid grid;
    private JFrame frame;
    private GameState gameState = GameState.STOPPED;
    private Timer timer;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JButton increaseDelayButton;
    private JButton decreaseDelayButton;
    private JToggleButton toggleButton;
    private int boardWidthInTiles;
    private int boardHeightInTiles;
    private int timerDelay = 200;
    private JLabel generationLabel;
    private JLabel delayLabel;
    private JComboBox<String> comboBox;
    private final Theme theme = new Theme();

    public Game(int pixelsWidth, int pixelsHeight, int tileSize) {
        this.boardWidthInTiles = pixelsWidth / tileSize;
        this.boardHeightInTiles = pixelsHeight / tileSize;
        initializeGUI(tileSize);
        listenToEvent(tileSize);
    }

    private void initializeGUI(int tileSize) {
        // create frame and pixel board
        frame = new JFrame("Game of Life");
        board = new Board(boardWidthInTiles, boardHeightInTiles, tileSize);

        // create buttons
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");
        increaseDelayButton = new JButton("+");
        decreaseDelayButton = new JButton("-");
        toggleButton = new JToggleButton("Grid ON");
        toggleButton.setSelected(true);

        // create labels
        generationLabel = new JLabel("Generation: 0");
        delayLabel = new JLabel("Delay (ms): " + timerDelay);
        JLabel themeLabel = new JLabel("Theme: ");

        // create dropdown list
        String[] options = {"Vanilla", "Fire", "Blueprint"};
        comboBox = new JComboBox<>(options);

        // add components to frame
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
        frame.setVisible(true);
    }

    private void listenToEvent(int tileSize){
        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                toggleButton.setText("Grid ON");
                board.setGridColor(theme.getThemeGridColor());
                board.repaint();
            } else {
                toggleButton.setText("Grid OFF");
                toggleButton.setBackground(Color.RED);
                board.setGridColor(null);
                board.repaint();
            }
        });

        comboBox.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            switch (Objects.requireNonNull(selectedOption)) {
                case "Vanilla" -> {
                    theme.setTheme(ThemeName.VANILLA, toggleButton.isSelected());
                    board.setTheme(theme);
                }
                case "Fire" -> {
                    theme.setTheme(ThemeName.FIRE, toggleButton.isSelected());
                    board.setTheme(theme);
                }
                case "Blueprint" -> {
                    theme.setTheme(ThemeName.BLUEPRINT, toggleButton.isSelected());
                    board.setTheme(theme);
                }
            }
        });

        startButton.addActionListener(e -> {
            if (gameState == GameState.STOPPED) {
                startGame();
            } else if (gameState == GameState.PAUSED) {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                resumeGame();
            }
        });

        stopButton.addActionListener(e -> {
            if (gameState == GameState.RUNNING) {
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
                board.setGameState(gameState);
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
    }

    private void startGame() {
        if (gameState == GameState.PAUSED) {
            resumeGame();
        } else {
            grid = new Grid(boardWidthInTiles, boardHeightInTiles);
            setUserAliveCells();
        }
        gameState = GameState.RUNNING;
        board.setGameState(gameState);
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
        board.setGameState(gameState);
        timer.stop();
    }

    private void resumeGame() {
        gameState = GameState.RUNNING;
        board.setGameState(gameState);
        setUserAliveCells();
        timer.start();
    }

    private void stopGame() {
        gameState = GameState.STOPPED;
        board.setGameState(gameState);
        timer.stop();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        board.clearBoard();
    }

    private void sendMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
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
