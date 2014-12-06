/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.core;

import com.github.daytron.flipit.data.ComputerAIDifficulty;
import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.data.Score;
import com.github.daytron.flipit.player.PlayerManager;
import com.github.daytron.flipit.player.ComputerAI;
import java.util.List;
import javafx.scene.canvas.Canvas;

/**
 *
 * @author ryan
 */
public class Game {

    private boolean isGameRunning;
    private final PlayerType player1;
    private final PlayerType player2;
    private final String player1Color;
    private final String player2Color;

    private final PlayerManager playerManager;
    private final TurnEvaluator mapManager;

    private final ComputerAI comAI;
    private int[] aiChosenPlayTile;
    

    public Game(Canvas canvas, Map map, 
            PlayerType player1, PlayerType player2, 
            String player1Color, String player2Color) {
        
        this.isGameRunning = false;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Color = player1Color;
        this.player2Color = player2Color;

        this.mapManager = new TurnEvaluator(canvas, map, player1, player2, 
                player1Color, player2Color);

        // the true value means human start first (turn)
        this.playerManager = new PlayerManager(true);
        this.comAI = new ComputerAI(ComputerAIDifficulty.EASY);
    }

    public void play() {
        // Generate and draw the selected map 
        this.mapManager.generateMap();
        // Toggle game running flag to true
        this.isGameRunning = true;

        // Create players
        this.playerManager.createPlayers(
                this.player1Color, this.player2Color, 
                this.player1, this.player2, 
                this.mapManager.getPlayer1StartPos().clone(), 
                this.mapManager.getPlayer2StartPos().clone());

        // On first turn only if it goes for human player, it begins highlighting 
        // possible moves.
        // This is only called once, if and only if human is the first turn, 
        // this is regularly called/monitored in endTurn() method
        PlayerType player;
        if (this.playerManager.getTurn() == PlayerType.HUMAN) {
            // HUMAN PLAYER
            player = PlayerType.HUMAN;
            if (!this.mapManager.isTherePossibleMove(
                    this.playerManager.getOccupiedTiles(player),
                    this.playerManager.getEnemyTiles(player),
                    this.playerManager,
                    player)) {
                this.endGame();
                return;
            }

        } else {
            // COMPUTER PLAYER
            player = PlayerType.COMPUTER;
            if (this.mapManager.isTherePossibleMove(
                    this.playerManager.getOccupiedTiles(player), 
                    this.playerManager.getEnemyTiles(player),
                    this.playerManager,
                    player)) {
                this.aiChosenPlayTile = this.comAI.play(
                    this.playerManager.getPossibleMovePos(player),
                    this.playerManager.getPossibleAttackPos(player))
                        .clone();

                // Extra measure if there is no selected move from AI
                if (this.aiChosenPlayTile.length == 0 || this.aiChosenPlayTile == null) {
                    this.endGame();
                }

                // Apply move
                this.analyzeTile(PlayerType.COMPUTER,
                        this.aiChosenPlayTile[0],
                        this.aiChosenPlayTile[1]);
            }

        }
    }

    public void getClick(double x, double y) {
        if (this.isGameRunning) {
            if (this.playerManager.getTurn() == PlayerType.HUMAN) {
                this.computeMove(x, y, PlayerType.HUMAN);

            }
        }

    }

    // Flexible for both computer and human players
    public void computeMove(double x, double y, PlayerType player) {
        if (this.mapManager.isInsideTheGrid(x, y)) {

            // Convert raw mouse click position into grid positions [column x row]
            int[] grid_pos = this.mapManager.getTilePosition(x, y);

            // calls anaylze tile
            this.analyzeTile(player, grid_pos[0], grid_pos[1]);
        }
    }

    private void analyzeTile(PlayerType player, int x, int y) {
        // if not boulder tile continue analyzing
        if (!this.mapManager.isBoulderTile(x, y)) {
            // if not self-occupied tile continue analyzing
            if (!this.mapManager.isTilePartOf(
                    new Integer[]{x, y}, 
                    this.playerManager.getOccupiedTiles(player))) {

                // Check if tile is part of enemy territory
                if (this.mapManager.isTilePartOf(
                        new Integer[]{x, y}, 
                        this.playerManager.getOccupiedTiles(
                                this.getOpposingPlayer(player)))) {
                    // if yes then calculate attack move
                    if (this.isValidAttackMove(player, x, y)) {

                        // calculate how many possible tiles to attack
                        List<Integer[]> tilesToFlip = 
                                this.mapManager.getHowManyEnemyTilesToFlip(
                                    this.playerManager.getOccupiedTiles(player), 
                                    this.playerManager.getEnemyTiles(player));

                        // Call an attack move
                        this.flipTile(tilesToFlip, player);
                    }
                } else {
                    // if no, more likely a neutral tile and calculate if the move is valid
                    if (this.isValidOccupyMove(player, x, y)) {
                        // Call an occupy move
                        this.occupyTile(player, x, y);
                    }
                }
            }
        }
    }

    private PlayerType getOpposingPlayer(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return PlayerType.COMPUTER;
        } else {
            return PlayerType.HUMAN;
        }
    }

    // TODO
    private void flipTile(List<Integer[]> tilesToFlip, PlayerType player) {

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

            /*
             // 4. Remove newly occupied tile from higlight list 
             // To stop repainting the said tile when painted back to neutral color
             if (player.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
             this.mapManager.removeNewlyFlippedTileFromHighlightList(enemyTile.clone());
             } */
            
            // Ends game if tile flipped is an enemy base
            if (enemyTile[0] == this.playerManager.getPlayerMainBasePos(this.getOpposingPlayer(player))[0]
                    && enemyTile[1] == this.playerManager.getPlayerMainBasePos(this.getOpposingPlayer(player))[1]) {
                this.updateScore(player, Score.CHECKMATE.getScore());
                this.endGame();
                return;
            }
            
        }

    // 5. Update score accordingly
        this.updateScore(player, tilesToFlip.size());

        // 6. End Turn
        this.endTurn(player);

    }

    private boolean isValidAttackMove(PlayerType player, int x, int y) {
        return this.mapManager.isValidAttackMove(x, y, 
                this.playerManager.getOccupiedTiles(player));
    }

    private void occupyTile(PlayerType player, int x, int y) {

        // 1. paint a tile
        this.mapManager.paintTile(this.playerManager.getPlayerLightEdgeColor(player),
                this.playerManager.getPlayerMainColor(player),
                this.playerManager.getPlayerShadowEdgeColor(player),
                x - 1, y - 1);

        // 2. add tile to player occupied list
        this.playerManager.addTileToPlayerList(x, y, player);

        // 3. add score by 1
        this.updateScore(player, Score.ONE_TILE_OCCUPY.getScore());

        /*
         // 4. Remove newly occupied tile from higlight list 
         // To stop repainting the said tile when painted back to neutral color
         if (player.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
         this.mapManager.removeNewlyOccupiedTileFromHighlightList(new Integer[]{x, y});
         } */
        // 5. End turn
        this.endTurn(player);

    }

    private boolean isValidOccupyMove(PlayerType player, int x, int y) {
        return this.mapManager.isValidOccupyMove(this.playerManager.getOccupiedTiles(player), x, y);
    }

    private void updateScore(PlayerType player, int score) {
        this.playerManager.updateScore(player, score);
    }

    private void endTurn(PlayerType player) {
        // Check if there is still possible move, if there isn't then end game
        // Otherwise change turn
        if (!this.mapManager.isTherePossibleMove(
                this.playerManager.getOccupiedTiles(player),
                this.playerManager.getEnemyTiles(player),
                this.playerManager,
                player)) {
            this.endGame();
            return;
        } else {
            // change turn
            this.playerManager.nextTurn(player);
        }

        // For AI
        if (this.playerManager.getTurn() == PlayerType.COMPUTER) {

            if (this.mapManager.isTherePossibleMove(
                    this.playerManager.getOccupiedTiles(PlayerType.COMPUTER), 
                    this.playerManager.getEnemyTiles(PlayerType.COMPUTER),
                    this.playerManager,
                    player)) {
                
                this.aiChosenPlayTile = this.comAI.play(
                    this.playerManager.getPossibleMovePos(player), 
                    this.playerManager.getPossibleAttackPos(player));

                // Extra measure if there is no selected move from AI
                if (this.aiChosenPlayTile.length == 0 || this.aiChosenPlayTile == null) {
                    this.endGame();
                }

                // Appply move
                this.analyzeTile(PlayerType.COMPUTER,
                        this.aiChosenPlayTile[0],
                        this.aiChosenPlayTile[1]);

            } else {
                this.endGame();
            }

        } else {
            if (this.mapManager.isTherePossibleMove(
                    this.playerManager.getOccupiedTiles(PlayerType.HUMAN), 
                    this.playerManager.getEnemyTiles(PlayerType.HUMAN),
                    this.playerManager,
                    player)) {
                //this.mapManager.highlightPossibleHumanMovesUponAvailableTurn(GlobalSettings.PLAYER_OPTION_HUMAN);
            } else {
                this.endGame();
            }
        }

    }

    // TODO
    public void endGame() {
        this.isGameRunning = false;

        int human_score = this.playerManager.getScore(PlayerType.HUMAN);
        int computer_score = this.playerManager.getScore(PlayerType.COMPUTER);

        System.out.println("Your score: " + human_score);
        System.out.println("Computer score: " + computer_score);

        if (human_score > computer_score) {
            System.out.println("You win!");
        } else if (human_score < computer_score) {
            System.out.println("You lose!");
        } else {
            System.out.println("It's a tie!");
        }
    }

}
