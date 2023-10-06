package com.model;


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
    private final List<Point> selectedCoordinates;
    private boolean isMousePressed = false;
    private int prevX, prevY;

    private double zoomFactor = 1;

    public Board(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.GRAY);
        selectedCoordinates = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                prevX = e.getX();
                prevY = e.getY();
                addCoordinate(prevX, prevY);
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
                    int x = e.getX();
                    int y = e.getY();
                    addCoordinate(x, y);
                    prevX = x;
                    prevY = y;
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

    private void addCoordinate(int x, int y) {
        selectedCoordinates.add(new Point(x, y));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.scale(zoomFactor, zoomFactor);
        g2.transform(at);
        for (Point point : selectedCoordinates) {
            g.fillRect(point.x, point.y, 1, 1);
        }
    }

    public List<Point> getSelectedCoordinates() {
        return selectedCoordinates;
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

    public void updateBoardSize(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        repaint();
    }
}


