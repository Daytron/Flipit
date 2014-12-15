/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.player;

import com.github.daytron.flipit.data.PlayerType;
import com.github.daytron.flipit.data.ColorProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ryan
 */
class Player {

    private final PlayerType playerType;
    private int score;

    private final List<Integer[]> occupiedTiles;
    
    private final int[] mainBase;

    private final String light_edge_color;
    private final String main_color;
    private final String shadow_edge_color;
    
    private final ColorProperty playerColor;
    
    private List<Integer[]> possibleMovePos;
    private List<Integer[]> possibleAttackPos;
    
    private int turnsLeft;

    /**
     *
     * @param turn boolean [true] if human is first, otherwise [false]
     * @param playerType Options could be [Human] or [Computer]
     * @param playerSide Options could be [player1] or [player2]
     * @param main_color
     * @param startPos
     */
    public Player(PlayerType playerType, 
            String main_color, int[] startPos, int maxTurn) {
        this.score = 0;
        this.mainBase = startPos.clone();
        
        this.playerType = playerType;

        if (main_color.equals(ColorProperty.PLAYER_BLUE.getColor())) {
            this.light_edge_color = ColorProperty.PLAYER_BLUE_LIGHT_EDGE.getColor();
            this.shadow_edge_color = ColorProperty.PLAYER_BLUE_SHADOW_EDGE.getColor();
            this.playerColor = ColorProperty.PLAYER_BLUE;
        } else {
            this.light_edge_color = ColorProperty.PLAYER_RED_LIGHT_EDGE.getColor();
            this.shadow_edge_color = ColorProperty.PLAYER_RED_SHADOW_EDGE.getColor();
            this.playerColor = ColorProperty.PLAYER_RED;
        }

        this.main_color = main_color;
        this.occupiedTiles = new ArrayList<>();
        
        this.turnsLeft = maxTurn;
    }

    public int[] getMainBase() {
        return mainBase;
    }

    
    
    public void addOccupiedTile(int x, int y) {
        this.occupiedTiles.add(new Integer[]{x, y});
    }
    
    public boolean removeOccupiedTile(int x, int y) {
        for (Integer[] tile : this.occupiedTiles) {
            if (tile[0] == x && tile[1] == y) {
                this.occupiedTiles.remove(tile);
                return true;
            }
        }
        
        return false;
    }

    public List<Integer[]> getOccupiedTiles() {
        return occupiedTiles;
    }
    
    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public String getMain_color() {
        return main_color;
    }

    public String getLight_edge_color() {
        return light_edge_color;
    }

    public String getShadow_edge_color() {
        return shadow_edge_color;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void resetPossibleAttackPos() {
        this.possibleAttackPos = new ArrayList<>();
    }

    public void resetPossibleMovePos() {
        this.possibleMovePos = new ArrayList<>();
    }
    
    public void addPossibleAttackPos(Integer[] pos) {
        if (pos != null) {
            this.possibleAttackPos.add(pos);
        }
    }
    
    public void addPossibleMovePos(Integer[] pos) {
        if (pos != null) {
           this.possibleMovePos.add(pos);
        }
    }

    public List<Integer[]> getPossibleAttackPos() {
        return possibleAttackPos;
    }

    public List<Integer[]> getPossibleMovePos() {
        return possibleMovePos;
    }

    public ColorProperty getPlayerColor() {
        return playerColor;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }
    
    public void reduceTurnByOne() {
        if (this.turnsLeft > 1) {
           this.turnsLeft -= 1;
        }
        
    }

}
