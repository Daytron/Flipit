/* 
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.daytron.flipit.core;

import com.github.daytron.flipit.data.Difficulty;
import com.github.daytron.flipit.data.EndGameMessage;
import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.data.Score;
import com.github.daytron.flipit.data.GameProperty;
import com.github.daytron.flipit.player.PlayerManager;
import com.github.daytron.flipit.player.ComputerAI;
import com.github.daytron.simpledialogfx.data.DialogType;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import java.util.List;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;

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
    private final Overseer turnEvaluator;

    private final ComputerAI comAI;
    private int[] aiChosenPlayTile;

    private final int maxPlayerTurn;
    
    private Timeline comTimelineDelay;

    public Game(Canvas canvas, Map map,
            PlayerType player1, PlayerType player2,
            String player1Color, String player2Color) {

        this.isGameRunning = false;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Color = player1Color;
        this.player2Color = player2Color;

        // Randomly choose player for first turn
        Random r = new Random();
        boolean isHumanFirst;
        isHumanFirst = r.nextInt(2) == 1;
        this.playerManager = new PlayerManager(isHumanFirst);

        this.turnEvaluator = new Overseer(canvas, map, player1, player2,
                player1Color, player2Color);

        this.comAI = new ComputerAI(Difficulty.EASY);

        this.maxPlayerTurn = GameProperty.MAX_TURNS.getValue();
    }

    public void play() {
        // Create players
        this.playerManager.createPlayers(
                this.player1Color, this.player2Color,
                this.player1, this.player2,
                this.turnEvaluator.getPlayer1StartPos().clone(),
                this.turnEvaluator.getPlayer2StartPos().clone(),
                this.maxPlayerTurn);

        // Clear canvas
        this.turnEvaluator.clearCanvas();

        // Draw score labels
        this.turnEvaluator.drawScoreLabel(
                this.playerManager.getPlayerLeftScoreLabel(),
                this.playerManager.getPlayerRightScoreLabel());

        // Generate and draw the selected map 
        this.turnEvaluator.generateMap();
        // Toggle game running flag to true
        this.isGameRunning = true;

        // On first turn only if it goes for human player, it begins highlighting 
        // possible moves.
        // This is only called once, if and only if human is the first turn, 
        // this is regularly called/monitored in endTurn() method
        PlayerType player;
        if (this.playerManager.getTurn() == PlayerType.HUMAN) {
            // HUMAN PLAYER
            player = PlayerType.HUMAN;
            if (!this.turnEvaluator.isTherePossibleMove(
                    this.playerManager.getOccupiedTiles(player),
                    this.playerManager.getEnemyTiles(player),
                    this.playerManager,
                    player)) {
                this.endGame();
                return;
            }

            this.turnEvaluator.displayTurnStatus(player);

        } else {
            // COMPUTER PLAYER
            player = PlayerType.COMPUTER;
            if (this.turnEvaluator.isTherePossibleMove(
                    this.playerManager.getOccupiedTiles(player),
                    this.playerManager.getEnemyTiles(player),
                    this.playerManager,
                    player)) {

                this.turnEvaluator.displayTurnStatus(player);

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
        if (this.turnEvaluator.isInsideTheGrid(x, y)) {

            // Convert raw mouse click position into grid positions [column x row]
            int[] grid_pos = this.turnEvaluator.getTilePosition(x, y);

            // calls anaylze tile
            this.analyzeTile(player, grid_pos[0], grid_pos[1]);
        }
    }

    private void analyzeTile(PlayerType player, int x, int y) {
        // if not boulder tile continue analyzing
        if (!this.turnEvaluator.isBoulderTile(x, y)) {
            // if not self-occupied tile continue analyzing
            if (!this.turnEvaluator.isTilePartOf(
                    new Integer[]{x, y},
                    this.playerManager.getOccupiedTiles(player))) {

                // Check if tile is part of enemy territory
                if (this.turnEvaluator.isTilePartOf(
                        new Integer[]{x, y},
                        this.playerManager.getOccupiedTiles(
                                this.getOpposingPlayer(player)))) {
                    // if yes then calculate attack move
                    if (this.isValidAttackMove(player, x, y)) {

                        // calculate how many possible tiles to attack
                        List<Integer[]> tilesToFlip
                                = this.turnEvaluator.getHowManyEnemyTilesToFlip(
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
            this.turnEvaluator.attackTile(
                    this.playerManager.getPlayerColor(player),
                    enemyTile[0] - 1, enemyTile[1] - 1);

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
        this.updateScore(player, tilesToFlip.size() * Score.ONE_TILE_FLIP.getScore());

        // 6. End Turn
        this.endTurn(player);

    }

    private boolean isValidAttackMove(PlayerType player, int x, int y) {
        return this.turnEvaluator.isValidAttackMove(x, y,
                this.playerManager.getOccupiedTiles(player));
    }

    private void occupyTile(PlayerType player, int x, int y) {

        // 1. paint a tile
        this.turnEvaluator.paintTile(this.playerManager.getPlayerLightEdgeColor(player),
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
        return this.turnEvaluator.isValidOccupyMove(this.playerManager.getOccupiedTiles(player), x, y);
    }

    private void updateScore(PlayerType player, int score) {
        this.playerManager.updateScore(player, score);

        int newScore = this.playerManager.getScore(player);
        int turnLeft = this.playerManager.getPlayerTurnsLeft(player);

        this.turnEvaluator.updateScore(newScore, turnLeft, player);
    }

    private void endTurn(PlayerType player) {
        // Check if there is still possible move, if there isn't then end game
        // Otherwise change turn
        if (!this.turnEvaluator.isTherePossibleMove(
                this.playerManager.getOccupiedTiles(player),
                this.playerManager.getEnemyTiles(player),
                this.playerManager,
                player)) {
            this.endGame();
            return;
        } else {
            // Update turn left
            this.playerManager.reducePlayerTurnByOne(player);
            int newScore = this.playerManager.getScore(player);
            int enemyTurnLeft = this.playerManager.getPlayerTurnsLeft(
                this.playerManager.getEnemyOf(player));
            int turnLeft = this.playerManager.getPlayerTurnsLeft(player);

            // Display new turn left label
            this.turnEvaluator.updateScore(newScore, turnLeft, player);

            // If both players used all up all their turns, end the game
            if (turnLeft < 1 && enemyTurnLeft < 1) {
                this.endGame();
                return;
            }
            
            // End turn
            this.playerManager.nextTurn(player);
        }

        // Display turn
        this.turnEvaluator.displayTurnStatus(this.playerManager.getTurn());

        // For AI
        if (this.playerManager.getTurn() == PlayerType.COMPUTER) {
            // Add a delay before computer play
            this.comTimelineDelay = new Timeline(new KeyFrame(
                    Duration.millis(GameProperty.COM_PLAY_DELAY.getValue()),
                    ae -> playComputerTurn(player)));
            this.comTimelineDelay.play();
        } else {
            if (this.turnEvaluator.isTherePossibleMove(
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

    private void playComputerTurn(PlayerType player) {
        if (this.turnEvaluator.isTherePossibleMove(
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
    }

    // TODO
    public void endGame() {
        if (this.comTimelineDelay != null) {
            this.comTimelineDelay.stop();
        }
        
        this.isGameRunning = false;

        // Clear turn status label
        this.turnEvaluator.clearTurnStatus();

        int human_score = this.playerManager.getScore(PlayerType.HUMAN);
        int computer_score = this.playerManager.getScore(PlayerType.COMPUTER);

        String endGameMessage;
        
        if (human_score > computer_score) {
            endGameMessage = EndGameMessage.WIN.getMessage();
        } else if (human_score < computer_score) {
            endGameMessage = EndGameMessage.LOSE.getMessage();
        } else {
            endGameMessage = EndGameMessage.TIE.getMessage();
        }
        
        Dialog dialog = new Dialog(
                DialogType.GENERIC_OK,
                endGameMessage, 
                "You: " + human_score + "\nCom:" + computer_score);
        dialog.show();
    }

}
