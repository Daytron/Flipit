/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.engine;

import com.github.daytron.flipit.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author ryan
 */
public class GameManager {
    
    private Canvas canvas;
    private Map map;
    private String player1;
    private String player2;
    private String player1Color;
    private String player2Color;
    
    private GraphicsContext graphics;
    private MapManager mapManager;

    public GameManager(Canvas canvas, Map map, String player1, String player2, String player1Color, String player2Color) {
        this.canvas = canvas;
        this.map = map;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Color = player1Color;
        this.player2Color = player2Color;
        
        this.graphics  = canvas.getGraphicsContext2D();
        this.mapManager = new MapManager(canvas, map, player1, player2, player1Color, player2Color);
    }
    
    public void play() {
        
    }
    
    public void generateMap() {
        this.mapManager.generateMap(graphics);
    }
    
    
    
    public void getClick(double x, double y) {
        
    }
}
