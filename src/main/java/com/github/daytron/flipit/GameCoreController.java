/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import com.github.daytron.flipit.engine.MapManager;
import com.github.daytron.flipit.engine.GameManager;
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
    private GameManager gameManager;
    private MapManager mapManager;
    
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
        // Create new instance of game
        this.gameManager = new GameManager(canvas, this.app.getGamePreloader().getMapSelected(), this.app.getGamePreloader().getPlayer1Selected(), this.app.getGamePreloader().getPlayer2Selected(), this.app.getGamePreloader().getPlayer1ColorSelected(), this.app.getGamePreloader().getPlayer2ColorSelected());
        
        this.gameManager.generateMap();
        this.gameManager.play();
    }

    @FXML
    private void onClick(MouseEvent event) {
        this.gameManager.getClick(event.getX(), event.getY());
    }
    
    
}
