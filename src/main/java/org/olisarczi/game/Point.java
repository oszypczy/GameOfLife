package org.olisarczi.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Point {
    public int x;
    public int y;

    @Override
    public String toString() {
        return "{x: " + x + ", y: " + y + "}";
    }
}
