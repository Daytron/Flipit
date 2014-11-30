/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.player;

import com.github.daytron.flipit.utility.GlobalSettings;
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

    public void createPlayers(String p1Color, String p2Color, String player1, 
            String player2, int[] player1StartPos, int[] player2StartPos) {
        if (player1.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
            this.humanPlayer = new Player(this.isHumanFirst,
                    GlobalSettings.PLAYER_OPTION_HUMAN,
                    player1,
                    p1Color, player1StartPos);
            
            // Add the main base position as the first occupied tile list
            this.humanPlayer.addOccupiedTile(player1StartPos[0], player1StartPos[1]);
            
            this.computerPlayer = new Player(!this.isHumanFirst,
                    GlobalSettings.PLAYER_OPTION_COMPUTER,
                    player2,
                    p2Color, player2StartPos);
            
            // Add the main base position as the first occupied tile list
            this.computerPlayer.addOccupiedTile(player2StartPos[0], player2StartPos[1]);
            
        } else {
            this.humanPlayer = new Player(this.isHumanFirst,
                    GlobalSettings.PLAYER_OPTION_HUMAN,
                    player2,
                    p2Color, player2StartPos);
            
            // Add the main base position as the first occupied tile list
            this.humanPlayer.addOccupiedTile(player2StartPos[0], player2StartPos[1]);
            
            this.computerPlayer = new Player(!this.isHumanFirst,
                    GlobalSettings.PLAYER_OPTION_COMPUTER,
                    player1,
                    p1Color, player1StartPos);
            
            // Add the main base position as the first occupied tile list
            this.computerPlayer.addOccupiedTile(player1StartPos[0], player1StartPos[1]);
        }

    }

    private Player filterPlayer(String player) {
        if (player.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
            return this.humanPlayer;
        } else {
            return this.computerPlayer;
        }
    }
    
    public int[] getPlayerMainBasePos(String player) {
        return this.filterPlayer(player).getMainBase();
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

    public void addTileToPlayerList(int x, int y, String player) {
        this.filterPlayer(player).addOccupiedTile(x, y);
    }

    public void removeTileFromPlayerList(int x, int y, String player) {
        if (!this.filterPlayer(player).removeOccupiedTile(x, y)) {
            System.out.println("ERROR BUG: Cannot remove tile " + "[" + x + "," + y + "] for player: " + player);
        }
    }

    public List<Integer[]> getOccupiedTiles(String player) {
        return this.filterPlayer(player).getOccupiedTiles();
    }

    public List<Integer[]> getEnemyTiles(String player) {
        if (player.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
            return this.computerPlayer.getOccupiedTiles();
        } else {
            return this.humanPlayer.getOccupiedTiles();
        }
    }
    
    public String getEnemyOf(String player) {
        if (player.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
            return GlobalSettings.PLAYER_OPTION_COMPUTER;
        } else  {
            return GlobalSettings.PLAYER_OPTION_HUMAN;
        }
    }

    public boolean isHumanTurn() {
        return this.humanPlayer.isTurn();
    }

    public void updateScore(String player, int score) {
        this.filterPlayer(player).addScore(score);
    }

    public void nextTurn(String player) {
        if (player.equalsIgnoreCase(GlobalSettings.PLAYER_OPTION_HUMAN)) {
            this.humanPlayer.setTurn(false);
            this.computerPlayer.setTurn(true);

            // add something to trigger AI action
        } else {
            this.computerPlayer.setTurn(false);
            this.humanPlayer.setTurn(true);
        }
    }

}
