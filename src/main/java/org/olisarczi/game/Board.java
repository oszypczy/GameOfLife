package org.olisarczi.game;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
    @Getter
    private final List<Point> selectedCoordinates;
    private boolean isMousePressed = false;
    private int prevTileX, prevTileY;

    private double zoomFactor = 1;

    @Setter
    private int boardWidthInTiles;
    @Setter
    private int boardHeightInTiles;

    private final int tileSize;

    public Board(int widthInTiles, int heightInTiles, int tileSize) {
        this.boardWidthInTiles = widthInTiles;
        this.boardHeightInTiles = heightInTiles;
        this.tileSize = tileSize;
        setPreferredSize(new Dimension(widthInTiles * tileSize, heightInTiles * tileSize));
        setBackground(Color.GRAY);
        selectedCoordinates = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                prevTileX = e.getX() / tileSize; // Convert pixel coordinates to tile coordinates
                prevTileY = e.getY() / tileSize; // Convert pixel coordinates to tile coordinates
                addCoordinate(prevTileX, prevTileY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMousePressed = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMousePressed) {
                    int tileX = e.getX() / tileSize; // Convert pixel coordinates to tile coordinates
                    int tileY = e.getY() / tileSize; // Convert pixel coordinates to tile coordinates
                    if (tileX != prevTileX || tileY != prevTileY) {
                        addCoordinate(tileX, tileY);
                        prevTileX = tileX;
                        prevTileY = tileY;
                    }
                }
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                double scaleFactor = Math.pow(1.1, -rotation);
                zoomFactor *= scaleFactor;
                repaint();
            }
        });
    }

    private void addCoordinate(int tileX, int tileY) {
        selectedCoordinates.add(new Point(tileX, tileY));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.scale(zoomFactor, zoomFactor);
        g2.transform(at);

        // Draw grid lines
        for (int x = 0; x < boardWidthInTiles; x++) {
            for (int y = 0; y < boardHeightInTiles; y++) {
                g.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }

        g.setColor(Color.WHITE);
        for (Point point : selectedCoordinates) {
            g.fillRect(point.x * tileSize, point.y * tileSize, tileSize, tileSize);
        }
    }

    public void setSelectedCoordinates(List<Point> aliveCellsCords) {
        this.selectedCoordinates.clear();
        this.selectedCoordinates.addAll(aliveCellsCords);
        repaint();
    }

    public void clearBoard() {
        this.selectedCoordinates.clear();
        repaint();
    }

    public void updateBoardSize() {
        setPreferredSize(new Dimension(boardWidthInTiles, boardHeightInTiles));
    }

}


