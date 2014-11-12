/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.players;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ryan
 */
public class Player {
    private String playerType;
    private int score;
    private boolean turn;
    
    private List<Integer[]> occupiedTiles;

    public Player(boolean turn, String playerType) {
        this.score = 0;
        this.turn = turn;
        this.playerType = playerType;
        this.occupiedTiles = new ArrayList<>();
    }
    
    public void addOccupiedTile(int x, int y) {
        this.occupiedTiles.add(new Integer[]{x,y});
    }
    

    public void setIsTurn(boolean isTurn) {
        this.turn = isTurn;
    }

    public int getScore() {
        return score;
    }

    public boolean isTurn() {
        return turn;
    }
    
    
    
}
