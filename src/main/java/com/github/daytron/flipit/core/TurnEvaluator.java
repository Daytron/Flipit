/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.core;

import com.github.daytron.flipit.data.AttackTileDirection;
import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.data.TileColor;
import com.github.daytron.flipit.data.MapProperty;
import com.github.daytron.flipit.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.canvas.Canvas;

/**
 *
 * @author ryan
 */
public class TurnEvaluator {

    private final int numberOfRows;
    private final int numberOfColumns;
    private final Canvas canvas;
    private final List<Double> rowCell;
    private final List<Double> columnCell;

    double preferredHeight;
    double preferredWidth;
    private double leftPadding;
    private double topPadding;

    private final Map selectedMap;

    private final PlayerType selectedPlayer1;
    private final PlayerType selectedPlayer2;

    private final String selectedPlayer1Color;
    private final String selectedPlayer2Color;

    //private int[] current_tile_pos;
    // For occupied to attack reference only
    private Integer[] occupiedTileToAttack;
    private AttackTileDirection attackDirectionFrom;

    private Graphics graphics;

    // For possible moves calculation
    //private List<Integer[]> possibleMovePos;
    //private List<Integer[]> possibleAttackPos;
    public TurnEvaluator(Canvas canvas, Map map, PlayerType player1,
            PlayerType player2, String player1Color, String player2Color) {

        this.canvas = canvas;
        this.rowCell = new ArrayList<>();
        this.columnCell = new ArrayList<>();
        this.numberOfRows = map.getSize()[1];
        this.numberOfColumns = map.getSize()[0];

        int tile_edge_effect;
        // Tile edge effect size init
        if (map.getSize()[0] < 8 && map.getSize()[1] < 8) {
            tile_edge_effect = MapProperty.TILE_EDGE_EFFECT_LARGE.getValue();
        } else {
            tile_edge_effect = MapProperty.TILE_EDGE_EFFECT_SMALL.getValue();
        }

        this.selectedMap = map;
        this.selectedPlayer1 = player1;
        this.selectedPlayer2 = player2;
        this.selectedPlayer1Color = player1Color;
        this.selectedPlayer2Color = player2Color;

        // Retrieve map padding
        int mapPadding = MapProperty.MAP_PADDING.getValue();
        int mapTopPadding = MapProperty.MAP_TOP_PADDING.getValue();
        
        double canvasWidth = this.canvas.getWidth();
        double canvasHeight = this.canvas.getHeight();
        
        double mapWidth = canvasWidth - (mapPadding * 2);
        double mapHeight = canvasHeight - mapTopPadding - mapPadding;

        this.preferredHeight = ((int) mapHeight / this.numberOfRows) * (double) this.numberOfRows;
        this.preferredWidth = ((int) mapWidth / this.numberOfColumns) * (double) this.numberOfColumns;

        // Padding space for width and height
        this.leftPadding = (canvasWidth - preferredWidth) / 2;
        this.topPadding = mapTopPadding;

        // space between each cell
        double gridXSpace = this.preferredWidth / this.numberOfColumns;
        double gridYSpace = this.preferredHeight / this.numberOfRows;

        // generate rows
        for (double yi = this.topPadding;
                yi <= (canvasHeight - mapPadding);
                yi = yi + gridYSpace) {
            //gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }

        // generate columns
        for (double xi = this.leftPadding;
                xi <= (canvasWidth - this.leftPadding);
                xi = xi + gridXSpace) {
            //gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }

        this.graphics = new Graphics(canvas, gridXSpace, gridYSpace,
                rowCell, columnCell,
                tile_edge_effect);
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

            if (this.selectedPlayer1Color.equals(
                    TileColor.PLAYER_BLUE.getColor())) {
                tile_type = "player_blue";
            } else {
                tile_type = "player_red";
            }
        }

        if (this.selectedMap.getListOfPlayer2StartPosition()[0] == column + 1
                && this.selectedMap.getListOfPlayer2StartPosition()[1] == row + 1) {

            if (this.selectedPlayer2Color.equals(
                    TileColor.PLAYER_BLUE.getColor())) {
                tile_type = "player_blue";
            } else {
                tile_type = "player_red";
            }
        }

        switch (tile_type) {
            case "boulder":
                switch (type) {
                    case 1:
                        tile_color = TileColor.TILE_BOULDER_LIGHT_EDGE.getColor();
                        break;
                    case 2:
                        tile_color = TileColor.TILE_BOULDER_MAIN.getColor();
                        break;
                    case 3:
                        tile_color = TileColor.TILE_BOULDER_SHADOW_EDGE.getColor();
                        break;
                }
                break;

            case "player_blue":
                switch (type) {
                    case 1:
                        tile_color = TileColor.PLAYER_BLUE_LIGHT_EDGE.getColor();
                        break;
                    case 2:
                        tile_color = TileColor.PLAYER_BLUE.getColor();
                        break;
                    case 3:
                        tile_color = TileColor.PLAYER_BLUE_SHADOW_EDGE.getColor();
                        break;
                }
                break;

            case "player_red":
                switch (type) {
                    case 1:
                        tile_color = TileColor.PLAYER_RED_LIGHT_EDGE.getColor();
                        break;
                    case 2:
                        tile_color = TileColor.PLAYER_RED.getColor();
                        break;
                    case 3:
                        tile_color = TileColor.PLAYER_RED_SHADOW_EDGE.getColor();
                        break;
                }
                break;

            case "neutral":
                switch (type) {
                    case 1:
                        tile_color = TileColor.TILE_NEUTRAL_LIGHT_EDGE.getColor();
                        break;
                    case 2:
                        tile_color = TileColor.TILE_NEUTRAL_MAIN.getColor();
                        break;
                    case 3:
                        tile_color = TileColor.TILE_NEUTRAL_SHADOW_EDGE.getColor();
                        break;
                }
                break;
        }

        return tile_color;
    }

    public void generateMap() {
        // Resets and clear canvas (If player restart game)
        this.graphics.clearCanvas();
        
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

    public void paintTile(String light_edge_color, String main_color,
            String shadow_edge_color, int count_column, int count_row) {

        this.graphics.paintTile(light_edge_color,
                main_color, shadow_edge_color,
                count_column, count_row);
    }

    public int[] getPlayer1StartPos() {
        return this.selectedMap.getListOfPlayer1StartPosition();
    }

    public int[] getPlayer2StartPos() {
        return this.selectedMap.getListOfPlayer2StartPosition();
    }

    // Make sure isTherePossibleMove method is called first in GameManager 
    // or else possibleMovePos/possibleAttackMove is empty/old value
    public void highlightPossibleHumanMovesUponAvailableTurn(PlayerType player,
            PlayerManager playerManager) {
        if (player.equals(PlayerType.HUMAN)) {

            // For possible occupy move
            for (Integer[] tileMoveToHighlight : playerManager
                    .getPossibleMovePos(player)) {
                this.paintTile(
                        TileColor.TILE_POSSIBLE_MOVE_HIGHLIGHT_LiGHT_EDGE.getColor(),
                        TileColor.TILE_POSSIBLE_MOVE_HIGHLIGHT_MAIN.getColor(),
                        TileColor.TILE_POSSIBLE_MOVE_HIGHLIGHT_SHADOW_EDGE.getColor(),
                        tileMoveToHighlight[0] - 1,
                        tileMoveToHighlight[1] - 1);
            }

            // For possible attack move
            for (Integer[] tileAttackToHighlight : playerManager
                    .getPossibleMovePos(player)) {
                this.paintTile(
                        TileColor.TILE_POSSIBLE_ATTACK_HIGHLIGHT_LiGHT_EDGE.getColor(),
                        TileColor.TILE_POSSIBLE_ATTACK_HIGHLIGHT_MAIN.getColor(),
                        TileColor.TILE_POSSIBLE_ATTACK_HIGHLIGHT_SHADOW_EDGE.getColor(),
                        tileAttackToHighlight[0] - 1,
                        tileAttackToHighlight[1] - 1);
            }
        }
    }

    public void removeNewlyFlippedTileFromHighlightList(Integer[] tile,
            PlayerManager playerManager) {
        for (int i = 0; i < playerManager.getPossibleAttackPos(PlayerType.HUMAN)
                .size(); i++) {
            if (Objects.equals(tile[0], playerManager
                    .getPossibleAttackPos(PlayerType.HUMAN).get(i)[0])
                    && Objects.equals(tile[1],
                            playerManager.getPossibleAttackPos(PlayerType.HUMAN)
                            .get(i)[1])) {
                playerManager.getPossibleAttackPos(PlayerType.HUMAN).remove(i);
                break;
            }
        }
    }

    public void removeNewlyOccupiedTileFromHighlightList(Integer[] tile,
            PlayerManager playerManager) {
        for (int i = 0; i < playerManager.getPossibleMovePos(PlayerType.HUMAN)
                .size(); i++) {
            if (Objects.equals(tile[0],
                    playerManager.getPossibleMovePos(PlayerType.HUMAN).get(i)[0])
                    && Objects.equals(tile[1],
                            playerManager.getPossibleMovePos(PlayerType.HUMAN)
                            .get(i)[1])) {
                playerManager.getPossibleMovePos(PlayerType.HUMAN).remove(i);
                break;
            }
        }
    }

    public void removeHighlight(String enemy_light_color,
            String enemy_main_color, String enemy_shadow_color,
            PlayerManager playerManager) {

        System.out.println("is possible moves empty?: "
                + playerManager.getPossibleMovePos(PlayerType.HUMAN).isEmpty());

        // For possible move highlights
        for (Integer[] tileMoveToHighlight
                : playerManager.getPossibleMovePos(PlayerType.HUMAN)) {
            System.out.println("tile: [" + tileMoveToHighlight[0] + "," + tileMoveToHighlight[1] + "]");
            this.paintTile(TileColor.TILE_NEUTRAL_LIGHT_EDGE.getColor(),
                    TileColor.TILE_NEUTRAL_MAIN.getColor(),
                    TileColor.TILE_NEUTRAL_SHADOW_EDGE.getColor(),
                    tileMoveToHighlight[0] - 1,
                    tileMoveToHighlight[1] - 1);
        }

        // For possible attack highlights
        for (Integer[] tileAttackToHighlight
                : playerManager.getPossibleAttackPos(PlayerType.HUMAN)) {
            this.paintTile(enemy_light_color, enemy_main_color,
                    enemy_shadow_color,
                    tileAttackToHighlight[0] - 1,
                    tileAttackToHighlight[1] - 1);
        }

    }

    public boolean isTherePossibleMove(List<Integer[]> occupiedPlayerTiles,
            List<Integer[]> enemyPlayerTiles, PlayerManager playerManager,
            PlayerType player) {
        boolean isThereAMove = false;

        // Resets values
        playerManager.resetPossibleAttackAndMovePos(player);

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
                            playerManager.addPossibleMovePos(player, tile.clone());
                            isThereAMove = true;
                        }
                    }
                }
            }

            // Checks each diagonal neighbor for possible attack move
            for (Integer[] tile : neighborAttackMoveTiles) {
                if (this.isTilePartOf(tile, enemyPlayerTiles)) {
                    playerManager.addPossibleAttackPos(player, tile.clone());
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
        List<AttackTileDirection> direction = new ArrayList<>();

        // NOTE FOR ALL SIDE NEIGHBORS: 
        // Greater than 1 because origin index is 1 not 0 (uses grid positions)
        // for the top left tile neighbor
        if (this.checkGenericDiagonalNeighborTopLeft(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x - 1, y - 1});
            direction.add(AttackTileDirection.DIRECTION_TOP_LEFT);
        }

        // for the top right tile neighbor
        if (this.checkGenericDiagonalNeighborTopRight(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x + 1, y - 1});
            direction.add(AttackTileDirection.DIRECTION_TOP_RIGHT);
        }

        // for the lower left tile neighbor
        if (this.checkGenericDiagonalNeighborLowerLeft(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x - 1, y + 1});
            direction.add(AttackTileDirection.DIRECTION_LOWER_LEFT);
        }

        // for the lower right tile neighbor
        if (this.checkGenericDiagonalNeighborLowerRight(x, y)) {
            nearbytilesToCheck.add(new Integer[]{x + 1, y + 1});
            direction.add(AttackTileDirection.DIRECTION_LOWER_RIGHT);
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

    public List<Integer[]> getHowManyEnemyTilesToFlip(
            List<Integer[]> occupiedTiles,
            List<Integer[]> enemyTiles) {
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
            case DIRECTION_TOP_LEFT:
                // Compute possible tile attack based on "linked diagonally" occupied tiles
                while (this.checkGenericDiagonalNeighborTopLeft(
                        currentOccupiedTileToCheck[0],
                        currentOccupiedTileToCheck[1])) {
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

                listOfEnemyTilesToFlip.add(new Integer[]{
                    currentEnemyTileToCheck[0],
                    currentEnemyTileToCheck[1]});
                enemy_count += 1;

                // Check the link enemy tiles 
                while (this.checkGenericDiagonalNeighborLowerRight(
                        currentEnemyTileToCheck[0],
                        currentEnemyTileToCheck[1])) {
                    if (enemy_count == occupied_count) {
                        break;
                    }

                    currentEnemyTileToCheck[0] += 1;
                    currentEnemyTileToCheck[1] += 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentEnemyTileToCheck[0],
                                currentEnemyTileToCheck[1]},
                            enemyTiles)) {

                        listOfEnemyTilesToFlip.add(new Integer[]{
                            currentEnemyTileToCheck[0],
                            currentEnemyTileToCheck[1]});
                        enemy_count += 1;
                    } else {
                        break;
                    }
                }

                break;

            case DIRECTION_TOP_RIGHT:
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

                listOfEnemyTilesToFlip.add(new Integer[]{
                    currentEnemyTileToCheck[0],
                    currentEnemyTileToCheck[1]});
                enemy_count += 1;

                while (this.checkGenericDiagonalNeighborLowerLeft(
                        currentEnemyTileToCheck[0],
                        currentEnemyTileToCheck[1])) {
                    if (enemy_count == occupied_count) {
                        break;
                    }

                    currentEnemyTileToCheck[0] -= 1;
                    currentEnemyTileToCheck[1] += 1;

                    if (this.isTilePartOf(
                            new Integer[]{currentEnemyTileToCheck[0],
                                currentEnemyTileToCheck[1]},
                            enemyTiles)) {

                        listOfEnemyTilesToFlip.add(new Integer[]{
                            currentEnemyTileToCheck[0],
                            currentEnemyTileToCheck[1]});
                        enemy_count += 1;
                    } else {
                        break;
                    }
                }

                break;

            case DIRECTION_LOWER_LEFT:
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
                // Initialise position based on direction 
                // (opposite of lower left : top right)
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

            case DIRECTION_LOWER_RIGHT:
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

    public boolean isValidOccupyMove(
            List<Integer[]> occupiedTiles, int x, int y) {
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
        return (x_pos >= this.leftPadding && x_pos <= (this.canvas.getWidth() - this.leftPadding))
                && (y_pos >= this.topPadding && y_pos <= (this.canvas.getWidth() - this.topPadding));
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

    public void attackTile(TileColor playerColor,
            final int count_column, final int count_row) {

        this.graphics.flipTile(playerColor,
                count_column, count_row);
    }
    
    public void displayTurnStatus(PlayerType player) {
        this.graphics.displayTurnStatus(player);
    }
    
    public void clearTurnStatus() {
        this.graphics.clearTurnStatus();
    }

}
