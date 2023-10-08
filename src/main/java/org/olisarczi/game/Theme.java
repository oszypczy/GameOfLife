package org.olisarczi.game;

import lombok.Getter;

import java.awt.*;

public class Theme {
    @Getter
    private Color backGroundColor;
    @Getter
    private Color aliveCellColor;
    @Getter
    private Color currentGridColor;
    @Getter
    private Color themeGridColor;

    public Theme(){
        setTheme(ThemeName.VANILLA, true);
    }

    public void setTheme(ThemeName theme, boolean isGridOn){
        switch (theme) {
            case VANILLA -> {
                this.backGroundColor = Color.GRAY;
                this.aliveCellColor = Color.WHITE;
                if (isGridOn) {
                    this.currentGridColor = Color.BLACK;
                } else {
                    this.currentGridColor = null;
                }
                this.themeGridColor = Color.BLACK;
            }
            case FIRE -> {
                this.backGroundColor = Color.BLACK;
                this.aliveCellColor = Color.ORANGE;
                if (isGridOn) {
                    this.currentGridColor = Color.GRAY;
                } else {
                    this.currentGridColor = null;
                }
                this.themeGridColor = Color.GRAY;
            }
            case BLUEPRINT -> {
                this.backGroundColor = new Color(0, 32, 128);
                this.aliveCellColor = Color.YELLOW;
                if (isGridOn) {
                    this.currentGridColor = Color.DARK_GRAY;
                } else {
                    this.currentGridColor = null;
                }
                this.themeGridColor = Color.DARK_GRAY;
            }
        }
    }
}
