/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.engine;

import com.github.daytron.flipit.GlobalSettingsManager;
import com.github.daytron.flipit.Map;
import com.github.daytron.flipit.players.PlayerManager;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author ryan
 */
public class GameManager {

    private boolean isGameRunning;
    private Canvas canvas;
    private String player1;
    private String player2;
    private String player1Color;
    private String player2Color;

    private PlayerManager playerManager;
    private GraphicsContext graphics;
    private MapManager mapManager;

    public GameManager(Canvas canvas, Map map, String player1, String player2, String player1Color, String player2Color) {
        this.isGameRunning = false;
        this.canvas = canvas;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Color = player1Color;
        this.player2Color = player2Color;

        this.mapManager = new MapManager(canvas, map, player1, player2, player1Color, player2Color);

        // the true value means human start first (turn)
        this.playerManager = new PlayerManager(true);
    }

    public void play() {
        this.isGameRunning = true;

        // Create players
        this.playerManager.createPlayers(this.player1Color, this.player2Color, this.player1, this.player2);

        // Add start tile for each player to their lists.
        if (this.player1.equalsIgnoreCase(GlobalSettingsManager.PLAYER_OPTION_HUMAN)) {
            this.playerManager.addTileToPlayerList(this.mapManager.getPlayer1StartPos()[0],
                    this.mapManager.getPlayer1StartPos()[1],
                    GlobalSettingsManager.PLAYER_OPTION_HUMAN);
            this.playerManager.addTileToPlayerList(this.mapManager.getPlayer2StartPos()[0],
                    this.mapManager.getPlayer2StartPos()[1],
                    GlobalSettingsManager.PLAYER_OPTION_COMPUTER);
        } else {
            this.playerManager.addTileToPlayerList(this.mapManager.getPlayer1StartPos()[0],
                    this.mapManager.getPlayer1StartPos()[1],
                    GlobalSettingsManager.PLAYER_OPTION_COMPUTER);
            this.playerManager.addTileToPlayerList(this.mapManager.getPlayer2StartPos()[0],
                    this.mapManager.getPlayer2StartPos()[1],
                    GlobalSettingsManager.PLAYER_OPTION_HUMAN);
        }

        // On first turn only if it goes for human player, it begins highlighting 
        // possible moves.
        // This is only called once, if and only if human is the first turn, 
        // this is regularly called/monitored in endTurn() method
        if (this.playerManager.isHumanTurn()) {
            this.mapManager.highlightPossibleHumanMovesUponAvailableTurn(GlobalSettingsManager.PLAYER_OPTION_HUMAN);
        }
    }

    public void generateMap() {
        this.mapManager.generateMap();
    }

    public void getClick(double x, double y) {
        if (this.isGameRunning) {
            if (this.playerManager.isHumanTurn()) {
                this.computeMove(x, y, GlobalSettingsManager.PLAYER_OPTION_HUMAN);

            }
        }

    }

    // Flexible for both computer and human players
    public void computeMove(double x, double y, String player) {
        if (this.mapManager.isInsideTheGrid(x, y)) {
            // get the current tile position
            this.mapManager.setCurrentTile(x, y);

            // calls anaylze tile
            this.analyzeTile(player);
        }
    }

    private void analyzeTile(String player) {
        // if not boulder tile continue analyzing
        if (!this.mapManager.isBoulderTile()) {
            // if not self-occupied tile continue analyzing
            if (!this.mapManager.isSelfOccupiedTile(this.playerManager.getOccupiedTiles(player))) {
                if (this.mapManager.isEnemyOccupiedTile(this.playerManager.getOccupiedTiles(this.getOpposingPlayer(player)))) {
                    // if yes then calculate attack move
                    if (this.isValidAttackMove(player)) {
                        // Get the reference to the occupied tile to attack
                        Integer[] attackingTilePos = this.getAttackingTilePos().clone();

                        // calculate how many possible tiles to attack
                        List<Integer[]> tilesToFlip = this.mapManager.getHowManyEnemyTilesToFlip(this.playerManager.getOccupiedTiles(player), this.playerManager.getEnemyTiles(player));

                        // Call an attack move
                        this.flipTile(attackingTilePos, tilesToFlip, player);
                    }
                } else {
                    // if no, more likely a neutral tile and calculate if the move is valid
                    if (this.isValidOccupyMove(player)) {
                        // Call an occupy move
                        this.occupyTile(player);
                    }
                }
            }
        }
    }

    private String getOpposingPlayer(String player) {
        if (player.equalsIgnoreCase(GlobalSettingsManager.PLAYER_OPTION_HUMAN)) {
            return GlobalSettingsManager.PLAYER_OPTION_COMPUTER;
        } else {
            return GlobalSettingsManager.PLAYER_OPTION_HUMAN;
        }
    }

    private Integer[] getAttackingTilePos() {
        return this.mapManager.getOccupiedTileToAttack();
    }

    // TODO
    private void flipTile(Integer[] attackingTilePos, List<Integer[]> tilesToFlip, String player) {

        for (Integer[] enemyTile : tilesToFlip) {
            // 1. Paint tiles as your newly occupied tiles
            this.mapManager.paintTile(this.playerManager.getPlayerLightEdgeColor(player),
                    this.playerManager.getPlayerMainColor(player),
                    this.playerManager.getPlayerShadowEdgeColor(player),
                    enemyTile[0] - 1,
                    enemyTile[1] - 1);

            // 2. Add tiles to your list
            this.playerManager.addTileToPlayerList(enemyTile[0], enemyTile[1], player);

            // 3. Remove tiles from enemy's list
            this.playerManager.removeTileFromPlayerList(enemyTile[0], enemyTile[1], this.playerManager.getEnemyOf(player));
        }

        // 4. Update score accordingly
        this.updateScore(player, tilesToFlip.size());

        // 5. Check if this is a winning move
        // - e.g. no more more moves available or
        // enemy main base is hit
        // 6. end turn or end game depending of the outcome of 5
    }

    private boolean isValidAttackMove(String player) {
        return this.mapManager.isValidAttackMove(this.playerManager.getOccupiedTiles(player));
    }

    private void occupyTile(String player) {

        // 1. paint a tile
        this.mapManager.paintTile(this.playerManager.getPlayerLightEdgeColor(player),
                this.playerManager.getPlayerMainColor(player),
                this.playerManager.getPlayerShadowEdgeColor(player),
                this.mapManager.getCurrentTile()[0] - 1,
                this.mapManager.getCurrentTile()[1] - 1);

        // 2. add tile to player occupied list
        this.playerManager.addTileToPlayerList(this.mapManager.getCurrentTile()[0],
                this.mapManager.getCurrentTile()[1], player);

        // 3. add score by 1
        this.updateScore(player, GlobalSettingsManager.SCORE_ONE_TILE_OCCUPY);

        // 4. end turn
        this.endTurn(player);
    }

    private boolean isValidOccupyMove(String player) {
        return this.mapManager.isValidOccupyMove(this.playerManager.getOccupiedTiles(player));
    }

    private void updateScore(String player, int score) {
        this.playerManager.updateScore(player, score);
    }

    private void endTurn(String player) {
        // 
        this.playerManager.nextTurn(player);
        this.mapManager.highlightPossibleHumanMovesUponAvailableTurn(player);
    }

    // TODO
    public void checkEndGame() {

    }

}
