/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.engine;

import com.github.daytron.flipit.GlobalSettingsManager;
import com.github.daytron.flipit.Map;
import com.github.daytron.flipit.players.PlayerManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author ryan
 */
public class GameManager {
    
    private Canvas canvas;
    private String player1;
    private String player2;
    private String player1Color;
    private String player2Color;
    
    private PlayerManager playerManager;
    private GraphicsContext graphics;
    private MapManager mapManager;

    public GameManager(Canvas canvas, Map map, String player1, String player2, String player1Color, String player2Color) {
        this.canvas = canvas;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        
        this.graphics  = canvas.getGraphicsContext2D();
        this.mapManager = new MapManager(canvas, map, player1, player2, player1Color, player2Color);
        this.playerManager = new PlayerManager(true);
    }
    
   
    
    
    public void play() {
        // Create players
        this.playerManager.createPlayers();
        
        // Add start tile for each player to their lists.
        if (this.player1.equalsIgnoreCase(GlobalSettingsManager.PLAYER_START_POSITION_OPTION_HUMAN)) {
            this.playerManager.addHumanStartTilePos(this.mapManager.getPlayer1StartPos()[0], this.mapManager.getPlayer1StartPos()[1]);
            this.playerManager.addComputerStartTilePos(this.mapManager.getPlayer1StartPos()[0], this.mapManager.getPlayer1StartPos()[1]);
            
        } else {
            this.playerManager.addComputerStartTilePos(this.mapManager.getPlayer1StartPos()[0], this.mapManager.getPlayer1StartPos()[1]);
            this.playerManager.addHumanStartTilePos(this.mapManager.getPlayer1StartPos()[0], this.mapManager.getPlayer1StartPos()[1]);
        }
    }
    
    public void generateMap() {
        this.mapManager.generateMap(graphics);
    }
    
    
    
    public void getClick(double x, double y) {
        this.mapManager.getTilePosition(x, y);
        
        /*
        System.out.println("Columns: ");
        for (Double posX : this.mapManager.getColumnCell()) {
            System.out.println(posX);
        }
        
        System.out.println("Rows: ");
        for (Double posY : this.mapManager.getRowCell()) {
            System.out.println(posY);
        } */
        if (this.playerManager.isHumanTurn()) {
            
        }
    }
}
