/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class GameCoreController implements Initializable {
    private MainApp app;
    
    @FXML
    private Canvas canvas;

    public void setApp(MainApp application) {
        this.app = application;
    }
    
    /**
     * Initialises the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void run() {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        MapManager mapManager = new MapManager(canvas, this.app.getGamePreloader().getMapSelected(), this.app.getGamePreloader().getPlayer1Selected(), this.app.getGamePreloader().getPlayer2Selected(), this.app.getGamePreloader().getPlayer1ColorSelected(), this.app.getGamePreloader().getPlayer2ColorSelected());
        
        mapManager.generateMap(graphics);
    }

    @FXML
    private void onClick(MouseEvent event) {
        System.out.println("x: " + event.getX());
        System.out.println("y: " + event.getY());
    }
    
    
}
