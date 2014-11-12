/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.engine;

import com.github.daytron.flipit.GlobalSettingsManager;

import com.github.daytron.flipit.Map;
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

    private final int TILE_EDGE_EFFECT_THICKNESS;
    private final String selectedPlayer1;
    private final String selectedPlayer2;

    private final String selectedPlayer1Color;
    private final String selectedPlayer2Color;

    public MapManager(Canvas canvas, Map map, String player1, String player2, String player1Color, String player2Color) {
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
        this.selectedPlayer1 = player1;
        this.selectedPlayer2 = player2;
        this.selectedPlayer1Color = player1Color;
        this.selectedPlayer2Color = player2Color;
    }

    // type 1: light edges color
    // type 2: main color
    // type 3: shadow color
    private String extractPositionColor(int column, int row, int type) {
        String tile_color = "";
        String tile_type = "neutral";

        for (Integer[] pos : this.selectedMap.getListOfBoulders()) {
            if (pos[0] == column + 1 && pos[1] == row + 1) {
                tile_type = "boulder";
            }
        }

        if (this.selectedMap.getListOfPlayer1StartPosition()[0] == column + 1
                && this.selectedMap.getListOfPlayer1StartPosition()[1] == row + 1) {

            if (this.selectedPlayer1Color.equals(GlobalSettingsManager.PLAYER_COLOR_BLUE)) {
                tile_type = "player_blue";
            } else {
                tile_type = "player_red";
            }
        }

        if (this.selectedMap.getListOfPlayer2StartPosition()[0] == column + 1
                && this.selectedMap.getListOfPlayer2StartPosition()[1] == row + 1) {

            if (this.selectedPlayer2Color.equals(GlobalSettingsManager.PLAYER_COLOR_BLUE)) {
                tile_type = "player_blue";
            } else {
                tile_type = "player_red";
            }
        }

        switch (tile_type) {
            case "boulder":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettingsManager.TILE_BOULDER_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = GlobalSettingsManager.TILE_BOULDER_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = GlobalSettingsManager.TILE_BOULDER_SHADOW_EDGE_COLOR;
                        break;
                }
                break;

            case "player_blue":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettingsManager.PLAYER_COLOR_BLUE_LIGHT_EDGE;
                        break;
                    case 2:
                        tile_color = GlobalSettingsManager.PLAYER_COLOR_BLUE;
                        break;
                    case 3:
                        tile_color = GlobalSettingsManager.PLAYER_COLOR_BLUE_SHADOW_EDGE;
                        break;
                }
                break;

            case "player_red":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettingsManager.PLAYER_COLOR_RED_LIGHT_EDGE;
                        break;
                    case 2:
                        tile_color = GlobalSettingsManager.PLAYER_COLOR_RED;
                        break;
                    case 3:
                        tile_color = GlobalSettingsManager.PLAYER_COLOR_RED_SHADOW_EDGE;
                        break;
                }
                break;

            case "neutral":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettingsManager.TILE_NEUTRAL_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = GlobalSettingsManager.TILE_NEUTRAL_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = GlobalSettingsManager.TILE_NEUTRAL_SHADOW_EDGE_COLOR;
                        break;
                }
                break;
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

                this.paintTile(this.extractPositionColor(count_column, count_row, 1),
                        this.extractPositionColor(count_column, count_row, 2),
                        this.extractPositionColor(count_column, count_row, 3),
                        gc, count_column, count_row);

            }
        }

    }

    /**
     * Method for painting the tile
     * @param light_edge_color A string hex color code
     * @param main_color A string hex color code
     * @param shadow_edge_color A string hex color code
     * @param gc GraphicsContext object for drawing
     * @param count_column int value of the column (x) position of the tile
     * @param count_row int value of the row (y) position of the tile
     */
    public void paintTile(String light_edge_color, String main_color, String shadow_edge_color, GraphicsContext gc,
            int count_column, int count_row) {
        // coloring the light top and left edges respectively
        gc.setFill(Color.web(light_edge_color));
        gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
        gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace);

        // coloring main tile body
        gc.setFill(Color.web(main_color));
        gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

        // coloring tile's shadow for bottom and right edges respectively
        gc.setFill(Color.web(shadow_edge_color));
        gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS);
        gc.fillRect(this.columnCell.get(count_column) + this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
    }

    public int[] getPlayer1StartPos() {
        return this.selectedMap.getListOfPlayer1StartPosition();
    }

    public int[] getPlayer2StartPos() {
        return this.selectedMap.getListOfPlayer2StartPosition();
    }


    public List<Double> getColumnCell() {
        return this.columnCell;
    }

    public List<Double> getRowCell() {
        return this.rowCell;
    }

}
