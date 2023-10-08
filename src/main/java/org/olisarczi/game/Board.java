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

    @Setter
    private Color aliveCellColor = Color.WHITE;
    @Setter
    private Color gridColor = Color.BLACK;

    private final int tileSize;

    private java.awt.Point zoomCenter = new java.awt.Point(0, 0);

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
                Point translatedPoint = translateMouseCoordinates(e.getPoint());
                prevTileX = translatedPoint.x;
                prevTileY = translatedPoint.y;
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
                    Point translatedPoint = translateMouseCoordinates(e.getPoint());
                    prevTileX = translatedPoint.x;
                    prevTileY = translatedPoint.y;
                    addCoordinate(prevTileX, prevTileY);
                    }
                }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                double scaleFactor = Math.pow(1.1, -rotation);
                zoomCenter = e.getPoint(); // Store the cursor position
                double newZoomFactor = zoomFactor * scaleFactor;
                zoomFactor = Math.max(newZoomFactor, 1);
                repaint();
            }
        });
    }

    private Point translateMouseCoordinates(Point coordinates) {
        int mouseX = coordinates.x;
        int mouseY = coordinates.y;

        int dx = mouseX - zoomCenter.x;
        int dy = mouseY - zoomCenter.y;

        int zoomedX = (int) (dx / zoomFactor);
        int zoomedY = (int) (dy / zoomFactor);

        int scaledX = zoomCenter.x + zoomedX;
        int scaledY = zoomCenter.y + zoomedY;

        int tileX = scaledX / tileSize;
        int tileY = scaledY / tileSize;

        return new Point(tileX, tileY);
    }

    private void addCoordinate(int tileX, int tileY) {
        selectedCoordinates.add(new Point(tileX, tileY));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Calculate the transformation matrix
        AffineTransform at = new AffineTransform();
        at.translate(zoomCenter.x, zoomCenter.y); // Translate to the cursor position
        at.scale(zoomFactor, zoomFactor); // Apply scaling
        at.translate(-zoomCenter.x, -zoomCenter.y); // Translate back to the original position
        g2.transform(at);

        g.setColor(aliveCellColor);
        for (Point point : selectedCoordinates) {
            g.fillRect(point.x * tileSize, point.y * tileSize, tileSize, tileSize);
        }

        g.setColor(gridColor);
        // Draw grid lines
        for (int x = 0; x < boardWidthInTiles; x++) {
            for (int y = 0; y < boardHeightInTiles; y++) {
                g.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
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
