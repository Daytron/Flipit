/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.player;

import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.data.TileColor;
import java.util.List;

/**
 *
 * @author ryan
 */
public class PlayerManager {

    private PlayerType playerTurn;

    private Player humanPlayer;
    private Player computerPlayer;

    public PlayerManager(boolean isHumanFirst) {
        if (isHumanFirst) {
            this.playerTurn = PlayerType.HUMAN;
        } else {
            this.playerTurn = PlayerType.COMPUTER;
        }
    }

    public void createPlayers(String p1Color, String p2Color, PlayerType player1,
            PlayerType player2, int[] player1StartPos, int[] player2StartPos) {
        if (player1 == PlayerType.HUMAN) {
            this.humanPlayer = new Player(player1,
                    p1Color, player1StartPos);

            // Add the main base position as the first occupied tile list
            this.humanPlayer.addOccupiedTile(player1StartPos[0], player1StartPos[1]);

            this.computerPlayer = new Player(player2,
                    p2Color, player2StartPos);

            // Add the main base position as the first occupied tile list
            this.computerPlayer.addOccupiedTile(player2StartPos[0], player2StartPos[1]);

        } else {
            this.humanPlayer = new Player(player2,
                    p2Color, player2StartPos);

            // Add the main base position as the first occupied tile list
            this.humanPlayer.addOccupiedTile(player2StartPos[0], player2StartPos[1]);

            this.computerPlayer = new Player(player1,
                    p1Color, player1StartPos);

            // Add the main base position as the first occupied tile list
            this.computerPlayer.addOccupiedTile(player1StartPos[0], player1StartPos[1]);
        }

    }

    private Player getPlayer(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return this.humanPlayer;
        } else {
            return this.computerPlayer;
        }
    }

    public int[] getPlayerMainBasePos(PlayerType player) {
        return this.getPlayer(player).getMainBase();
    }

    public int getScore(PlayerType player) {
        return this.getPlayer(player).getScore();
    }

    public String getPlayerLightEdgeColor(PlayerType player) {
        return this.getPlayer(player).getLight_edge_color();
    }

    public String getPlayerShadowEdgeColor(PlayerType player) {
        return this.getPlayer(player).getShadow_edge_color();
    }

    public String getPlayerMainColor(PlayerType player) {
        return this.getPlayer(player).getMain_color();
    }

    public void addTileToPlayerList(int x, int y, PlayerType player) {
        this.getPlayer(player).addOccupiedTile(x, y);
    }

    public void removeTileFromPlayerList(int x, int y, PlayerType player) {
        if (!this.getPlayer(player).removeOccupiedTile(x, y)) {
            System.out.println("ERROR BUG: Cannot remove tile " + "[" + x + "," + y + "] for player: " + player);
        }
    }

    public List<Integer[]> getOccupiedTiles(PlayerType player) {
        return this.getPlayer(player).getOccupiedTiles();
    }

    public List<Integer[]> getEnemyTiles(PlayerType player) {
        return this.getPlayer(this.getEnemyOf(player))
                .getOccupiedTiles();
    }

    public PlayerType getEnemyOf(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return PlayerType.COMPUTER;
        } else {
            return PlayerType.HUMAN;
        }
    }

    public PlayerType getTurn() {
        return this.playerTurn;
    }

    public void updateScore(PlayerType player, int score) {
        this.getPlayer(player).addScore(score);
    }

    public void nextTurn(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            this.playerTurn = PlayerType.COMPUTER;

            // add something to trigger AI action
        } else {
            this.playerTurn = PlayerType.HUMAN;
        }
    }

    public void addPossibleMovePos(PlayerType player, Integer[] pos) {
        this.getPlayer(player).addPossibleMovePos(pos);
    }

    public void resetPossibleAttackAndMovePos(PlayerType player) {
        this.getPlayer(player).resetPossibleAttackPos();
        this.getPlayer(player).resetPossibleMovePos();
    }

    public void addPossibleAttackPos(PlayerType player, Integer[] pos) {
        this.getPlayer(player).addPossibleAttackPos(pos);
    }

    public List<Integer[]> getPossibleMovePos(PlayerType player) {
        return this.getPlayer(player).getPossibleMovePos();
    }

    public List<Integer[]> getPossibleAttackPos(PlayerType player) {
        return this.getPlayer(player).getPossibleAttackPos();
    }
    
    public TileColor getPlayerColor(PlayerType player) {
        return this.getPlayer(player).getPlayerColor();
    }

}
