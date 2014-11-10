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
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

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
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        MapManager mapManager = new MapManager(canvas);
        mapManager.generateMap(graphics);
    }    

    @FXML
    private void onClick(MouseEvent event) {
        System.out.println(event.getX());
    }
    
    private void createGrid(GraphicsContext gc) {
       
        
    }
    
}
