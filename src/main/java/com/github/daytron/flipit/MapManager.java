/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author ryan
 */
public class MapManager {

    private final int numberOfRows;
    private final int numberOfColumns;
    private final Canvas canvas;
    private final List<Double> rowCell;
    private final List<Double> columnCell;
    private double gridXSpace;
    private double gridYSpace;
    
    private final Map selectedMap;
    private final int selectedPlayer;

    private int TILE_EDGE_EFFECT_THICKNESS;
    
    // Human player color blue tile
    private final static String TILE_HUMAN_START_LIGHT_EDGE_COLOR = "#4FB1FF";
    private final static String TILE_HUMAN_START_MAIN_COLOR = "#0073CF";
    private final static String TILE_HUMAN_START_SHADOW_EDGE_COLOR = "#004881";
    
    // AI player color red tile
    private final static String TILE_AI1_START_LIGHT_EDGE_COLOR = "#FF4F4F";
    private final static String TILE_AI1_START_MAIN_COLOR = "#CF0000";
    private final static String TILE_AI1_START_SHADOW_EDGE_COLOR = "#810000";
    
    private final static String TILE_P2_START_POS = "";
    private final static String TILE_P3_START_POS = "";
    private final static String TILE_P4_START_POS = "";

    private final static String TILE_NEUTRAL_LIGHT_EDGE_COLOR = "#FFFFFF";
    private final static String TILE_NEUTRAL_MAIN_COLOR = "#C6C6C6";
    private final static String TILE_NEUTRAL_SHADOW_EDGE_COLOR = "#000000";

    private final static String TILE_BOULDER_LIGHT_EDGE_COLOR = "#FFFFFF";
    private final static String TILE_BOULDER_MAIN_COLOR = "#4F4F4F";
    private final static String TILE_BOULDER_SHADOW_EDGE_COLOR = "#000000";

    public MapManager(Canvas canvas, Map map, int playerSelected) {
        this.canvas = canvas;
        this.rowCell = new ArrayList<>();
        this.columnCell = new ArrayList<>();
        this.numberOfRows = map.getSize()[1];
        this.numberOfColumns = map.getSize()[0];

        // Tile edge effect size init
        if (map.getSize()[0] < 8 && map.getSize()[1] < 8) {
            TILE_EDGE_EFFECT_THICKNESS = 2;
        } else {
            TILE_EDGE_EFFECT_THICKNESS = 1;
        }

        this.selectedMap = map;
        this.selectedPlayer = playerSelected;
    }

    // type 1: light edges color
    // type 2: main color
    // type 3: shadow color
    private String extractPositionColor(int column, int row, int type) {
        String tile_color = "";
        String tile_type = "neutral";
        
        for (Integer[] pos: this.selectedMap.getListOfBoulders()) {
            if (pos[0] == column + 1 && pos[1] == row + 1) {
                tile_type = "boulder";
            }
        }
        
        if (this.selectedMap.getListOfPlayer1StartPosition()[0] == column + 1 &&
                this.selectedMap.getListOfPlayer1StartPosition()[1] == row + 1) {
            
            if (this.selectedPlayer == 1) {
                tile_type = "player_human";
            } else {
                tile_type = "player_ai";
            }
        }
        
        if (this.selectedMap.getListOfPlayer2StartPosition()[0] == column + 1 &&
                this.selectedMap.getListOfPlayer2StartPosition()[1] == row + 1) {
            
            if (this.selectedPlayer == 1) {
                tile_type = "player_ai";
            } else {
                tile_type = "player_human";
            }
        }
        
        
        switch (tile_type) {
            case "boulder":
                switch (type) {
                    case 1:
                        tile_color = TILE_BOULDER_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = TILE_BOULDER_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = TILE_BOULDER_SHADOW_EDGE_COLOR;
                        break;
                }   break;
                
            case "player_human":
                switch (type) {
                    case 1:
                        tile_color = TILE_HUMAN_START_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = TILE_HUMAN_START_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = TILE_HUMAN_START_SHADOW_EDGE_COLOR;
                    break;
            }   break;    
                
            case "player_ai":
                switch (type) {
                    case 1:
                        tile_color = TILE_AI1_START_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = TILE_AI1_START_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = TILE_AI1_START_SHADOW_EDGE_COLOR;
                    break;
            }   break;
            
            case "neutral":
                switch (type) {
                    case 1:
                        tile_color = TILE_NEUTRAL_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = TILE_NEUTRAL_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = TILE_NEUTRAL_SHADOW_EDGE_COLOR;
                    break;
            }   break;
        }

        return tile_color;
    }

    public void generateMap(GraphicsContext gc) {
        double x = this.canvas.getWidth();
        double y = this.canvas.getHeight();

        double preferredHeight = ((int) y / this.numberOfRows) * (double) this.numberOfRows;
        double preferredWidth = ((int) x / this.numberOfColumns) * (double) this.numberOfColumns;

        // Padding space for width and height
        double halfPaddingWidth = (x - preferredWidth) / 2;
        double halfPaddingHeight = (y - preferredHeight) / 2;

        // space between each cell
        this.gridXSpace = preferredWidth / this.numberOfColumns;
        this.gridYSpace = preferredHeight / this.numberOfRows;

        gc.setLineWidth(2);

        // generate rows
        for (double yi = halfPaddingHeight; yi <= (y - halfPaddingHeight); yi = yi + this.gridYSpace) {
            //gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }

        // generate columns
        for (double xi = halfPaddingWidth; xi <= (x - halfPaddingWidth); xi = xi + this.gridXSpace) {
            //gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }

        // Fill grid tiles with neutral color
        for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
            for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {

                // coloring (for nuetral tiles) the light top and left edges respectively
                gc.setFill(Color.web(this.extractPositionColor(count_column, count_row, 1)));
                gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
                gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace);

                // coloring main tile body
                gc.setFill(Color.web(this.extractPositionColor(count_column, count_row, 2)));
                gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

                // coloring tile's shadow for bottom and right edges respectively
                gc.setFill(Color.web(this.extractPositionColor(count_column, count_row, 3)));
                gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS);
                gc.fillRect(this.columnCell.get(count_column) + this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
            }
        }

        
    }

    private void initNeutralTiles(int count_row, int count_column) {

    }

    public List<Double> getColumnCell() {
        return this.columnCell;
    }

    public List<Double> getRowCell() {
        return this.rowCell;
    }

}
