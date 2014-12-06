/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.player;

import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.utility.GlobalSettings;
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
        } else  {
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

    private Player filterPlayer(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return this.humanPlayer;
        } else {
            return this.computerPlayer;
        }
    }

    public Player getPlayer(PlayerType playerType) {
        if (playerType == PlayerType.HUMAN) {
            return this.humanPlayer;
        } else  {
            return this.computerPlayer;
        }
    }
    
    
    
    public int[] getPlayerMainBasePos(PlayerType player) {
        return this.filterPlayer(player).getMainBase();
    }

    public int getScore(PlayerType player) {
        return this.filterPlayer(player).getScore();
    }

    public String getPlayerLightEdgeColor(PlayerType player) {
        return this.filterPlayer(player).getLight_edge_color();
    }

    public String getPlayerShadowEdgeColor(PlayerType player) {
        return this.filterPlayer(player).getShadow_edge_color();
    }

    public String getPlayerMainColor(PlayerType player) {
        return this.filterPlayer(player).getMain_color();
    }

    public void addTileToPlayerList(int x, int y, PlayerType player) {
        this.filterPlayer(player).addOccupiedTile(x, y);
    }

    public void removeTileFromPlayerList(int x, int y, PlayerType player) {
        if (!this.filterPlayer(player).removeOccupiedTile(x, y)) {
            System.out.println("ERROR BUG: Cannot remove tile " + "[" + x + "," + y + "] for player: " + player);
        }
    }

    public List<Integer[]> getOccupiedTiles(PlayerType player) {
        return this.filterPlayer(player).getOccupiedTiles();
    }

    public List<Integer[]> getEnemyTiles(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return this.computerPlayer.getOccupiedTiles();
        } else {
            return this.humanPlayer.getOccupiedTiles();
        }
    }
    
    public PlayerType getEnemyOf(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return PlayerType.COMPUTER;
        } else  {
            return PlayerType.HUMAN;
        }
    }

    public PlayerType getTurn() {
        return this.playerTurn;
    }

    public void updateScore(PlayerType player, int score) {
        this.filterPlayer(player).addScore(score);
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
        if (player == PlayerType.HUMAN) {
            this.humanPlayer.addPossibleMovePos(pos);
        } else  {
            this.computerPlayer.addPossibleMovePos(pos);
        }
    }
    
    public void resetPossibleAttackAndMovePos(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            this.humanPlayer.resetPossibleAttackPos();
            this.humanPlayer.resetPossibleMovePos();
        } else {
            this.computerPlayer.resetPossibleAttackPos();
            this.computerPlayer.resetPossibleMovePos();
        }
    }
    
    public void addPossibleAttackPos(PlayerType player, Integer[] pos) {
        if (player == PlayerType.HUMAN) {
            this.humanPlayer.addPossibleAttackPos(pos);
        } else  {
            this.computerPlayer.addPossibleAttackPos(pos);
        }
    }
    
    public List<Integer[]> getPossibleMovePos(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return this.humanPlayer.getPossibleMovePos();
        } else {
            return this.computerPlayer.getPossibleMovePos();
        }
    }
    
    public List<Integer[]> getPossibleAttackPos(PlayerType player) {
        if (player == PlayerType.HUMAN) {
            return this.humanPlayer.getPossibleAttackPos();
        } else {
            return this.computerPlayer.getPossibleAttackPos();
        }
    }

}
