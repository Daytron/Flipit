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
import com.github.daytron.flipit.core.Game;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class GameController implements Initializable {
    private MainApp app;
    private Game game;
    
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
        this.game = new Game(canvas, 
                this.app.getGamePreloader().getMapSelected(), 
                this.app.getGamePreloader().getPlayer1(), 
                this.app.getGamePreloader().getPlayer2(), 
                this.app.getGamePreloader().getPlayer1ColorSelected(), 
                this.app.getGamePreloader().getPlayer2ColorSelected());
        
        this.game.play();
    }

    @FXML
    private void canvas_on_click(MouseEvent event) {
        this.game.getClick(event.getX(), event.getY());
    }

    @FXML
    private void restart_btn_on_click(ActionEvent event) {
        // Create new instance of game
        this.game = new Game(canvas, 
                this.app.getGamePreloader().getMapSelected(), 
                this.app.getGamePreloader().getPlayer1(), 
                this.app.getGamePreloader().getPlayer2(), 
                this.app.getGamePreloader().getPlayer1ColorSelected(), 
                this.app.getGamePreloader().getPlayer2ColorSelected());
        
        this.game.play();
    }

    @FXML
    private void new_game_btn_on_click(ActionEvent event) {
        this.game = null;
        this.app.viewNewGameSetup();
    }

    @FXML
    private void quit_btn_on_click(ActionEvent event) {
        Platform.exit();
    }
    
    
}
