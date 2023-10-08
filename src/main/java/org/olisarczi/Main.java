package org.olisarczi;

import org.olisarczi.game.Game;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game(700, 700, 12));
    }
}

// to do:
// - add tests
// - right click to kill cell
// - add patterns
// - better logging
