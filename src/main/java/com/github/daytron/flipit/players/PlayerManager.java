/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.players;

import com.github.daytron.flipit.GlobalSettingsManager;
import java.util.List;

/**
 *
 * @author ryan
 */
public class PlayerManager {

    private boolean isHumanFirst;

    private Player humanPlayer;
    private Player computerPlayer;

    public PlayerManager(boolean humanFirst) {
        this.isHumanFirst = humanFirst;
    }

    public void createPlayers(String p1Color, String p2Color, String player1, String player2) {
        if (player1.equalsIgnoreCase(GlobalSettingsManager.PLAYER_OPTION_HUMAN)) {
            this.humanPlayer = new Player(this.isHumanFirst,
                    GlobalSettingsManager.PLAYER_OPTION_HUMAN,
                    player1,
                    p1Color);
            this.computerPlayer = new Player(!this.isHumanFirst,
                    GlobalSettingsManager.PLAYER_OPTION_COMPUTER,
                    player2,
                    p2Color);
        } else {
            this.humanPlayer = new Player(this.isHumanFirst,
                    GlobalSettingsManager.PLAYER_OPTION_HUMAN,
                    player2,
                    p2Color);
            this.computerPlayer = new Player(!this.isHumanFirst,
                    GlobalSettingsManager.PLAYER_OPTION_COMPUTER,
                    player1,
                    p1Color);
        }

    }

    public Player filterPlayer(String player) {
        if (player.equalsIgnoreCase(GlobalSettingsManager.PLAYER_OPTION_HUMAN)) {
            return this.humanPlayer;
        } else {
            return this.computerPlayer;
        }
    }

    public int getScore(String player) {
        return this.filterPlayer(player).getScore();
    }
    
    public String getPlayerLightEdgeColor(String player) {
        return this.filterPlayer(player).getLight_edge_color();
    }

    public String getPlayerShadowEdgeColor(String player) {
        return this.filterPlayer(player).getShadow_edge_color();
    }

    public String getPlayerMainColor(String player) {
        return this.filterPlayer(player).getMain_color();
    }

    public void addTileToList(int x, int y, String player) {
        this.filterPlayer(player).addOccupiedTile(x, y);
    }

    public List<Integer[]> getOccupiedTiles(String player) {
        return this.filterPlayer(player).getOccupiedTiles();
    }

    // TODO
    public boolean verifyTileMove(int x, int y) {
        return true;
    }

    public void addTileToPlayer(int x, int y, String player) {
        if (verifyTileMove(x, y)) {
            this.filterPlayer(player).addOccupiedTile(x, y);
        }

    }

    public boolean isHumanTurn() {
        return this.humanPlayer.isTurn();
    }

    public void updateScore(String player, int score) {
        this.filterPlayer(player).addScore(score);
    }

    public void nextTurn(String player) {
        if (player.equalsIgnoreCase(GlobalSettingsManager.PLAYER_OPTION_HUMAN)) {
            this.humanPlayer.setTurn(false);
            this.computerPlayer.setTurn(true);

            // add something to trigger AI action
        } else {
            this.computerPlayer.setTurn(false);
            this.humanPlayer.setTurn(true);
        }
    }

}
