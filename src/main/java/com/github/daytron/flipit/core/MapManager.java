/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.core;

import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.utility.GlobalSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author ryan
 */
public class MapManager {

    private GraphicsContext gc;

    private final int numberOfRows;
    private final int numberOfColumns;
    private final Canvas canvas;
    private final List<Double> rowCell;
    private final List<Double> columnCell;
    private double gridXSpace;
    private double gridYSpace;

    double preferredHeight;
    double preferredWidth;
    private double halfPaddingWidth;
    private double halfPaddingHeight;

    private final Map selectedMap;

    private final int TILE_EDGE_EFFECT_THICKNESS;
    private final PlayerType selectedPlayer1;
    private final PlayerType selectedPlayer2;

    private final String selectedPlayer1Color;
    private final String selectedPlayer2Color;

    //private int[] current_tile_pos;
    // For occupied to attack reference only
    private Integer[] occupiedTileToAttack;
    private int attackDirectionFrom;

    // For possible moves calculation
    private List<Integer[]> possibleMovePos;
    private List<Integer[]> possibleAttackPos;

    public MapManager(Canvas canvas, Map map, PlayerType player1, 
            PlayerType player2, String player1Color, String player2Color) {
        this.gc = canvas.getGraphicsContext2D();

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

            if (this.selectedPlayer1Color.equals(GlobalSettings.PLAYER_COLOR_BLUE)) {
                tile_type = "player_blue";
            } else {
                tile_type = "player_red";
            }
        }

        if (this.selectedMap.getListOfPlayer2StartPosition()[0] == column + 1
                && this.selectedMap.getListOfPlayer2StartPosition()[1] == row + 1) {

            if (this.selectedPlayer2Color.equals(GlobalSettings.PLAYER_COLOR_BLUE)) {
                tile_type = "player_blue";
            } else {
                tile_type = "player_red";
            }
        }

        switch (tile_type) {
            case "boulder":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettings.TILE_BOULDER_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = GlobalSettings.TILE_BOULDER_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = GlobalSettings.TILE_BOULDER_SHADOW_EDGE_COLOR;
                        break;
                }
                break;

            case "player_blue":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettings.PLAYER_COLOR_BLUE_LIGHT_EDGE;
                        break;
                    case 2:
                        tile_color = GlobalSettings.PLAYER_COLOR_BLUE;
                        break;
                    case 3:
                        tile_color = GlobalSettings.PLAYER_COLOR_BLUE_SHADOW_EDGE;
                        break;
                }
                break;

            case "player_red":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettings.PLAYER_COLOR_RED_LIGHT_EDGE;
                        break;
                    case 2:
                        tile_color = GlobalSettings.PLAYER_COLOR_RED;
                        break;
                    case 3:
                        tile_color = GlobalSettings.PLAYER_COLOR_RED_SHADOW_EDGE;
                        break;
                }
                break;

            case "neutral":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = GlobalSettings.TILE_NEUTRAL_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR;
                        break;
                }
                break;
        }

        return tile_color;
    }

    public void generateMap() {
        double x = this.canvas.getWidth();
        double y = this.canvas.getHeight();

        this.preferredHeight = ((int) y / this.numberOfRows) * (double) this.numberOfRows;
        this.preferredWidth = ((int) x / this.numberOfColumns) * (double) this.numberOfColumns;

        // Padding space for width and height
        this.halfPaddingWidth = (x - preferredWidth) / 2;
        this.halfPaddingHeight = (y - preferredHeight) / 2;

        // space between each cell
        this.gridXSpace = this.preferredWidth / this.numberOfColumns;
        this.gridYSpace = this.preferredHeight / this.numberOfRows;

        gc.setLineWidth(2);

        // generate rows
        for (double yi = this.halfPaddingHeight; yi <= (y - this.halfPaddingHeight); yi = yi + this.gridYSpace) {
            //gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }

        // generate columns
        for (double xi = this.halfPaddingWidth; xi <= (x - this.halfPaddingWidth); xi = xi + this.gridXSpace) {
            //gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }

        // Fill grid tiles with neutral color
        for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
            for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {

                this.paintTile(this.extractPositionColor(count_column, count_row, 1),
                        this.extractPositionColor(count_column, count_row, 2),
                        this.extractPositionColor(count_column, count_row, 3),
                        count_column, count_row);

            }
        }

    }

    /**
     * Method for painting the tile
     *
     * @param light_edge_color A string hex color code
     * @param main_color A string hex color code
     * @param shadow_edge_color A string hex color code
     * @param count_column int value of the column (x) position of the tile
     * (with 0 as index)
     * @param count_row int value of the row (y) position of the tile (with 0 as
     * index)
     */
    public void paintTile(String light_edge_color, String main_color, String shadow_edge_color,
            int count_column, int count_row) {
        // coloring the light top and left edges respectively
        this.gc.setFill(Color.web(light_edge_color));
        this.gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.gridXSpace, TILE_EDGE_EFFECT_THICKNESS);
        this.gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace);

        // coloring main tile body
        this.gc.setFill(Color.web(main_color));
        this.gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);

        // coloring tile's shadow for bottom and right edges respectively
        this.gc.setFill(Color.web(shadow_edge_color));
        this.gc.fillRect(this.columnCell.get(count_column) + TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS, this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS);
        this.gc.fillRect(this.columnCell.get(count_column) + this.gridXSpace - TILE_EDGE_EFFECT_THICKNESS, this.rowCell.get(count_row) + TILE_EDGE_EFFECT_THICKNESS, TILE_EDGE_EFFECT_THICKNESS, this.gridYSpace - TILE_EDGE_EFFECT_THICKNESS);
    }

    public int[] getPlayer1StartPos() {
        return this.selectedMap.getListOfPlayer1StartPosition();
    }

    public int[] getPlayer2StartPos() {
        return this.selectedMap.getListOfPlayer2StartPosition();
    }

    // Make sure isTherePossibleMove method is called first in GameManager 
    // or else possibleMovePos/possibleAttackMove is empty/old value
    public void highlightPossibleHumanMovesUponAvailableTurn(String player) {
        if (player.equals(GlobalSettings.PLAYER_OPTION_HUMAN)) {

            // For possible occupy move
            for (Integer[] tileMoveToHighlight : this.possibleMovePos) {
                this.paintTile(GlobalSettings.TILE_POSSIBLE_MOVE_HIGHLIGHT_LiGHT_EDGE_COLOR,
                        GlobalSettings.TILE_POSSIBLE_MOVE_HIGHLIGHT_MAIN_COLOR,
                        GlobalSettings.TILE_POSSIBLE_MOVE_HIGHLIGHT_SHADOW_EDGE_COLOR,
                        tileMoveToHighlight[0] - 1,
                        tileMoveToHighlight[1] - 1);
            }

            // For possible attack move
            for (Integer[] tileAttackToHighlight : this.possibleAttackPos) {
                this.paintTile(GlobalSettings.TILE_POSSIBLE_ATTACK_HIGHLIGHT_LiGHT_EDGE_COLOR,
                        GlobalSettings.TILE_POSSIBLE_ATTACK_HIGHLIGHT_MAIN_COLOR,
                        GlobalSettings.TILE_POSSIBLE_ATTACK_HIGHLIGHT_SHADOW_EDGE_COLOR,
                        tileAttackToHighlight[0] - 1,
                        tileAttackToHighlight[1] - 1);
            }
        }
    }

    public void removeNewlyFlippedTileFromHighlightList(Integer[] tile) {
        for (int i = 0; i < this.possibleAttackPos.size(); i++) {
            if (Objects.equals(tile[0], this.possibleAttackPos.get(i)[0])
                    && Objects.equals(tile[1], this.possibleAttackPos.get(i)[1])) {
                this.possibleAttackPos.remove(i);
                break;
            }
        }
    }

    public void removeNewlyOccupiedTileFromHighlightList(Integer[] tile) {
        for (int i = 0; i < this.possibleMovePos.size(); i++) {
            if (Objects.equals(tile[0], this.possibleMovePos.get(i)[0])
                    && Objects.equals(tile[1], this.possibleMovePos.get(i)[1])) {
                this.possibleMovePos.remove(i);
                break;
            }
        }
    }

    public void removeHighlight(String enemy_light_color,
            String enemy_main_color, String enemy_shadow_color) {
        
        System.out.println("is possible moves empty?: " + this.possibleMovePos.isEmpty());
        
        // For possible move highlights
        for (Integer[] tileMoveToHighlight : this.possibleMovePos) {
            System.out.println("tile: [" + tileMoveToHighlight[0] + "," + tileMoveToHighlight[1] + "]" );
            this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                    GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                    GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                    tileMoveToHighlight[0] - 1,
                    tileMoveToHighlight[1] - 1);
        }

        // For possible attack highlights
        for (Integer[] tileAttackToHighlight : this.possibleAttackPos) {
            this.paintTile(enemy_light_color, enemy_main_color,
                    enemy_shadow_color,
                    tileAttackToHighlight[0] - 1,
                    tileAttackToHighlight[1] - 1);
        }

    }

    public List<Integer[]> getPossibleAttackPos() {
        return possibleAttackPos;
    }

    public List<Integer[]> getPossibleMovePos() {
        return possibleMovePos;
    }

    public boolean isTherePossibleMove(List<Integer[]> occupiedPlayerTiles,
            List<Integer[]> enemyPlayerTiles) {
        boolean isThereAMove = false;

        // Resets values
        this.possibleMovePos = new ArrayList<>();
        this.possibleAttackPos = new ArrayList<>();

        List<Integer[]> neighborOccupyMoveTiles;
        List<Integer[]> neighborAttackMoveTiles;

        // Check each occupied tile for any possible move
        for (Integer[] pos : occupiedPlayerTiles) {

            neighborOccupyMoveTiles = new ArrayList<>();
            neighborAttackMoveTiles = new ArrayList<>();

            // Get all neighbor tiles for occupy move
            // Left, right, top and bottom
            // For left neighbor tile
            if (this.checkGenericCrossNeighborLeft(pos[0])) {
                neighborOccupyMoveTiles.add(new Integer[]{pos[0] - 1, pos[1]});
            }

            // For right neighbor tile
            if (this.checkGenericCrossNeighborRight(pos[0])) {
                neighborOccupyMoveTiles.add(new Integer[]{pos[0] + 1, pos[1]});
            }

            // For top neighbor tile
            if (this.checkGenericCrossNeighborTop(pos[1])) {
                neighborOccupyMoveTiles.add(new Integer[]{pos[0], pos[1] - 1});
            }

            // For bottom neighbor tile
            if (this.checkGenericCrossNeighborBottom(pos[1])) {
                neighborOccupyMoveTiles.add(new Integer[]{pos[0], pos[1] + 1});
            }

            // Get all neighbor tiles for attack move
            // top left, top right, lower left, lower right
            // For top left neighbor tile
            if (this.checkGenericDiagonalNeighborTopLeft(pos[0], pos[1])) {
                neighborAttackMoveTiles.add(new Integer[]{pos[0] - 1, pos[1] - 1});
            }

            // For top right neighbor tile
            if (this.checkGenericDiagonalNeighborTopRight(pos[0], pos[1])) {
                neighborAttackMoveTiles.add(new Integer[]{pos[0] + 1, pos[1] - 1});
            }

            // For lower left neighbor tile
            if (this.checkGenericDiagonalNeighborLowerLeft(pos[0], pos[1])) {
                neighborAttackMoveTiles.add(new Integer[]{pos[0] - 1, pos[1] + 1});
            }

            // For lower right neighbor tile
            if (this.checkGenericDiagonalNeighborLowerRight(pos[0], pos[1])) {
                neighborAttackMoveTiles.add(new Integer[]{pos[0] + 1, pos[1] + 1});
            }

            // Checks each cross neighbor for possible occupy move
            for (Integer[] tile : neighborOccupyMoveTiles) {
                if (!this.isBoulderTile(tile[0], tile[1])) {
                    if (!this.isTilePartOf(tile, occupiedPlayerTiles)) {
                        if (!this.isTilePartOf(tile, enemyPlayerTiles)) {
                            this.possibleMovePos.add(tile.clone());
                            isThereAMove = true;
                        }
                    }
                }
            }

            // Checks each diagonal neighbor for possible attack move
            for (Integer[] tile : neighborAttackMoveTiles) {
                if (this.isTilePartOf(tile, enemyPlayerTiles)) {
                    this.possibleAttackPos.add(tile.clone());
                    isThereAMove = true;
                }
            }

        }

        return isThereAMove;
    }

    private boolean checkGenericDiagonalNeighborTopLeft(int x, int y) {
        return (x > 1) && (y > 1);
    }

    private boolean checkGenericDiagonalNeighborTopRight(int x, int y) {
        return (x < this.numberOfColumns) && (y > 1);
    }

    private boolean checkGenericDiagonalNeighborLowerLeft(int x, int y) {
        return (x > 1) && (y < this.numberOfRows);
    }

    private boolean checkGenericDiagonalNeighborLowerRight(int x, int y) {
        return (x < this.numberOfColumns) && (y < this.numberOfRows);
    }

    private boolean checkGenericCrossNeighborLeft(int x) {
        return x > 1;
    }

    private boolean checkGenericCrossNeighborTop(int y) {
        return y > 1;
    }

    private boolean checkGenericCrossNeighborRight(int x) {
        return x < this.numberOfColumns;
    }

    private boolean checkGenericCrossNeighborBottom(int y) {
        return y < this.numberOfRows;
    }

    public boolean isValidAttackMove(int x, int y, List<Integer[]> occupiedTiles) {
        List<Integer[]> nearbytilesToCheck = new ArrayList<>();
        List<Integer> direction = new ArrayList<>();

        // NOTE FOR ALL SIDE NEIGHBORS: 
        // Greater than 1 because origin index is 1 not 0 (uses grid positions)
        // for the top left tile neighbor
        if (this.checkGenericDiagonalNeighborTopLeft(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x - 1, y - 1});
            direction.add(GlobalSettings.DIRECTION_TOP_LEFT);
        }

        // for the top right tile neighbor
        if (this.checkGenericDiagonalNeighborTopRight(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x + 1, y - 1});
            direction.add(GlobalSettings.DIRECTION_TOP_RIGHT);
        }

        // for the lower left tile neighbor
        if (this.checkGenericDiagonalNeighborLowerLeft(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x - 1, y + 1});
            direction.add(GlobalSettings.DIRECTION_LOWER_LEFT);
        }

        // for the lower right tile neighbor
        if (this.checkGenericDiagonalNeighborLowerRight(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x + 1, y + 1});
            direction.add(GlobalSettings.DIRECTION_LOWER_RIGHT);
        }

        for (int i = 0; i < nearbytilesToCheck.size(); i++) {
            if (this.isTilePartOf(nearbytilesToCheck.get(i), occupiedTiles)) {
                this.occupiedTileToAttack = nearbytilesToCheck.get(i).clone();
                this.attackDirectionFrom = direction.get(i);
                return true;
            }
        }

        return false;
    }

    public boolean isTilePartOf(Integer[] tileToCheck, List<Integer[]> playerTiles) {
        for (Integer[] posOccupied : playerTiles) {
            if (Objects.equals(tileToCheck[0], posOccupied[0])
                    && Objects.equals(tileToCheck[1], posOccupied[1])) {
                return true;
            }
        }

        return false;
    }

    public Integer[] getOccupiedTileToAttack() {
        return occupiedTileToAttack;
    }

    public List<Integer[]> getHowManyEnemyTilesToFlip(List<Integer[]> occupiedTiles, List<Integer[]> enemyTiles) {
        List<Integer[]> listOfEnemyTilesToFlip = new ArrayList<>();

        int occupied_count = 1;
        int enemy_count = 0;
        int computed_count = 0;

        int[] currentOccupiedTileToCheck = new int[2];
        currentOccupiedTileToCheck[0] = this.occupiedTileToAttack[0];
        currentOccupiedTileToCheck[1] = this.occupiedTileToAttack[1];

        int[] currentEnemyTileToCheck = new int[2];
        currentEnemyTileToCheck[0] = this.occupiedTileToAttack[0];
        currentEnemyTileToCheck[1] = this.occupiedTileToAttack[1];

        switch (this.attackDirectionFrom) {
            case GlobalSettings.DIRECTION_TOP_LEFT:
                // Compute possible tile attack based on "linked diagonally" occupied tiles
                while (this.checkGenericDiagonalNeighborTopLeft(currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1])) {
                    currentOccupiedTileToCheck[0] -= 1;
                    currentOccupiedTileToCheck[1] -= 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1]},
                            occupiedTiles)) {
                        occupied_count += 1;
                    } else {
                        break;
                    }
                }

                // Compute enemy tiles linked together diagonally
                // Initialise position based on direction (opposite of top left : lower right)
                // Because default pos is from occupied tile not on enemy tile
                currentEnemyTileToCheck[0] += 1;
                currentEnemyTileToCheck[1] += 1;

                listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                enemy_count += 1;

                // Check the link enemy tiles 
                while (this.checkGenericDiagonalNeighborLowerRight(currentEnemyTileToCheck[0], currentEnemyTileToCheck[1])) {
                    if (enemy_count == occupied_count) {
                        break;
                    }

                    currentEnemyTileToCheck[0] += 1;
                    currentEnemyTileToCheck[1] += 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]},
                            enemyTiles)) {

                        listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                        enemy_count += 1;
                    } else {
                        break;
                    }
                }

                break;

            case GlobalSettings.DIRECTION_TOP_RIGHT:
                // Compute possible tile attack based on "linked diagonally" occupied tiles
                while (this.checkGenericDiagonalNeighborTopRight(currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1])) {
                    currentOccupiedTileToCheck[0] += 1;
                    currentOccupiedTileToCheck[1] -= 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1]},
                            occupiedTiles)) {
                        occupied_count += 1;
                    } else {
                        break;
                    }
                }

                // Compute enemy tiles linked together diagonally
                // Initialise position based on direction (opposite of top right : lower left)
                // Because default pos is from occupied tile not on enemy tile
                currentEnemyTileToCheck[0] -= 1;
                currentEnemyTileToCheck[1] += 1;

                listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                enemy_count += 1;

                while (this.checkGenericDiagonalNeighborLowerLeft(currentEnemyTileToCheck[0], currentEnemyTileToCheck[1])) {
                    if (enemy_count == occupied_count) {
                        break;
                    }

                    currentEnemyTileToCheck[0] -= 1;
                    currentEnemyTileToCheck[1] += 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]},
                            enemyTiles)) {

                        listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                        enemy_count += 1;
                    } else {
                        break;
                    }
                }

                break;

            case GlobalSettings.DIRECTION_LOWER_LEFT:
                // Compute possible tile attack based on "linked diagonally" occupied tiles
                while (this.checkGenericDiagonalNeighborLowerLeft(currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1])) {
                    currentOccupiedTileToCheck[0] -= 1;
                    currentOccupiedTileToCheck[1] += 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1]},
                            occupiedTiles)) {
                        occupied_count += 1;
                    } else {
                        break;
                    }
                }

                // Compute enemy tiles linked together diagonally
                // Initialise position based on direction (opposite of lower left : top right)
                // Because default pos is from occupied tile not on enemy tile
                currentEnemyTileToCheck[0] += 1;
                currentEnemyTileToCheck[1] -= 1;

                listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                enemy_count += 1;

                while (this.checkGenericDiagonalNeighborTopRight(currentEnemyTileToCheck[0], currentEnemyTileToCheck[1])) {
                    if (enemy_count == occupied_count) {
                        break;
                    }

                    currentEnemyTileToCheck[0] += 1;
                    currentEnemyTileToCheck[1] -= 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]},
                            enemyTiles)) {

                        listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                        enemy_count += 1;
                    } else {
                        break;
                    }
                }

                break;

            case GlobalSettings.DIRECTION_LOWER_RIGHT:
                // Compute possible tile attack based on "linked diagonally" occupied tiles
                while (this.checkGenericDiagonalNeighborLowerRight(currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1])) {
                    currentOccupiedTileToCheck[0] += 1;
                    currentOccupiedTileToCheck[1] += 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentOccupiedTileToCheck[0], currentOccupiedTileToCheck[1]},
                            occupiedTiles)) {
                        occupied_count += 1;
                    } else {
                        break;
                    }
                }

                // Compute enemy tiles linked together diagonally
                // Initialise position based on direction (opposite of lower right : top left)
                // Because default pos is from occupied tile not on enemy tile
                currentEnemyTileToCheck[0] -= 1;
                currentEnemyTileToCheck[1] -= 1;

                listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                enemy_count += 1;

                while (this.checkGenericDiagonalNeighborTopLeft(currentEnemyTileToCheck[0], currentEnemyTileToCheck[1])) {
                    if (enemy_count == occupied_count) {
                        break;
                    }

                    currentEnemyTileToCheck[0] -= 1;
                    currentEnemyTileToCheck[1] -= 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]},
                            enemyTiles)) {

                        listOfEnemyTilesToFlip.add(new Integer[]{currentEnemyTileToCheck[0], currentEnemyTileToCheck[1]});
                        enemy_count += 1;
                    } else {
                        break;
                    }
                }

                break;
        }

        return listOfEnemyTilesToFlip;
    }

    public boolean isValidOccupyMove(List<Integer[]> occupiedTiles, int x, int y) {
        List<Integer[]> nearbytilesToCheck = new ArrayList<>();

        // NOTE FOR ALL SIDE NEIGHBORS: 
        // Greater than 1 because origin index is 1 not 0 (uses grid positions)
        // for left tile neighbor
        if (this.checkGenericCrossNeighborLeft(x)) {
            nearbytilesToCheck.add(new Integer[]{x - 1, y});
        }

        // for top tile neighbor
        if (this.checkGenericCrossNeighborTop(y)) {
            nearbytilesToCheck.add(new Integer[]{x, y - 1});
        }

        // for right tile neighbor
        if (this.checkGenericCrossNeighborRight(x)) {
            nearbytilesToCheck.add(new Integer[]{x + 1, y});
        }

        // for bottom tile neighbor
        if (this.checkGenericCrossNeighborBottom(y)) {
            nearbytilesToCheck.add(new Integer[]{x, y + 1});
        }

        for (Integer[] posNeighbor : nearbytilesToCheck) {
            for (Integer[] posOccupied : occupiedTiles) {
                if (Objects.equals(posNeighbor[0], posOccupied[0])
                        && Objects.equals(posNeighbor[1], posOccupied[1])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Method for finding the tile position based on mouseClick. This method
     * simply made use of binary search algorithm.
     *
     * @param x_pos Mouseclick x position
     * @param y_pos Mouseclick y position
     * @return The position in relation to grid location [x column, y row]
     */
    public int[] getTilePosition(double x_pos, double y_pos) {
        int tile_x, tile_y;

        // For locating column position
        int highX = this.columnCell.size() - 1;
        int lowX = 0;
        int midX = 0;

        while (lowX <= highX) {
            midX = lowX + (highX - lowX) / 2;

            if (x_pos < this.columnCell.get(midX)) {
                highX = midX;
            } else if (x_pos > this.columnCell.get(midX)) {
                lowX = midX;
            } else {
                break;
            }

            if (lowX + 1 == highX) {
                break;
            }
        }

        // For locating row position
        tile_x = lowX + 1;

        int highY = this.rowCell.size() - 1;
        int lowY = 0;
        int midY = 0;

        while (lowY <= highY) {
            midY = lowY + (highY - lowY) / 2;

            if (y_pos < this.rowCell.get(midY)) {
                highY = midY;
            } else if (y_pos > this.rowCell.get(midY)) {
                lowY = midY;
            } else {
                break;
            }

            if (lowY + 1 == highY) {
                break;
            }
        }
        tile_y = lowY + 1;

        /*
         System.out.println("tile: [" + tile_x + "," + tile_y + "]" );
        
        
         System.out.println("X clicked: " + x_pos);
        
         System.out.println("low: " + lowX);
         System.out.println(this.columnCell.get(lowX));
        
         System.out.println("high: " + highX);
         System.out.println(this.columnCell.get(highX));
        
         System.out.println("mid: " + midX);
         */
        return new int[]{tile_x, tile_y};
    }

    /**
     * Checks whether the mouselick lands inside the playing grid map
     *
     * @param x_pos mouse x position
     * @param y_pos mouse y position
     * @return a boolean value true if it's inside, otherwise it returns false
     */
    public boolean isInsideTheGrid(double x_pos, double y_pos) {
        return (x_pos >= this.halfPaddingWidth && x_pos <= (this.canvas.getWidth() - this.halfPaddingWidth))
                && (y_pos >= this.halfPaddingHeight && y_pos <= (this.canvas.getWidth() - this.halfPaddingHeight));
    }

    /**
     * Method for analysing the tile property Legend: 1 - neutral tile 2 -
     * boulder tile 3 - human occupied tile 4 - enemy occupied tile
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isBoulderTile(int x, int y) {

        for (Integer[] boulderPos : this.selectedMap.getListOfBoulders()) {
            if (x == boulderPos[0] && y == boulderPos[1]) {
                return true;
            }
        }

        return false;
    }

    public List<Double> getColumnCell() {
        return this.columnCell;
    }

    public List<Double> getRowCell() {
        return this.rowCell;
    }

}