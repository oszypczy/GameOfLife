package org.olisarczi;

import org.olisarczi.game.Game;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Game(700, 700, 10));
    }
}

// to do:
// - add tests
// - add patterns
// - better logging
