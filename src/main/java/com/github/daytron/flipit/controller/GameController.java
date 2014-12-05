/* 
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera ryangilera@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.daytron.flipit.controller;

import com.github.daytron.flipit.*;
import com.github.daytron.flipit.core.MapManager;
import com.github.daytron.flipit.core.Game;
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
public class GameController implements Initializable {
    private MainApp app;
    private Game gameManager;
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
        this.gameManager = new Game(canvas, this.app.getGamePreloader().getMapSelected(), this.app.getGamePreloader().getPlayer1(), this.app.getGamePreloader().getPlayer2(), this.app.getGamePreloader().getPlayer1ColorSelected(), this.app.getGamePreloader().getPlayer2ColorSelected());
        
        this.gameManager.generateMap();
        this.gameManager.play();
    }

    @FXML
    private void onClick(MouseEvent event) {
        this.gameManager.getClick(event.getX(), event.getY());
    }
    
    
}