/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.players;

import com.github.daytron.flipit.GlobalSettingsManager;

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

    public void createPlayers() {
        this.humanPlayer = new Player(this.isHumanFirst, GlobalSettingsManager.PLAYER_START_POSITION_OPTION_HUMAN);
        this.computerPlayer = new Player(!this.isHumanFirst, GlobalSettingsManager.PLAYER_START_POSITION_OPTION_COMPUTER);
        
        
    }
    
     public void addHumanStartTilePos(int x, int y) {
         this.humanPlayer.addOccupiedTile(x, y);
    }
     
     public void addComputerStartTilePos(int x, int y) {
         this.computerPlayer.addOccupiedTile(x, y);
     }

    public boolean verifyTileMove(int x, int y) {
        return true;
    }

    public void addTileToPlayer(int x, int y, String player) {
        if (verifyTileMove(x, y)) {
            if (player.equalsIgnoreCase(GlobalSettingsManager.PLAYER_START_POSITION_OPTION_HUMAN)) {
                this.humanPlayer.addOccupiedTile(x, y);
            } else {
                this.computerPlayer.addOccupiedTile(x, y);
            }
        }

    }

    public boolean isHumanTurn() {
        return this.humanPlayer.isTurn();
    }
    
    

}
