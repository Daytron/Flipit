package com.github.daytron.flipit.controller;

import com.github.daytron.flipit.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController implements Initializable {
    private MainApp app;
    
    
    @FXML
    private Label title;
    @FXML
    private Button newGameButton;
    @FXML
    private Button rulesButton;
    @FXML
    private Button exitButton;
    
    public void setApp(MainApp application) {
        this.app = application;
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void clickNewGameButton(ActionEvent event) {
        // If there is no map json file found show a dialog instead
        if (this.app.getGamePreloader().isMapEmpty()) {
            app.showNoMapFoundDialog();
        } else   {
            app.viewNewGameSetup();
        }
         
    }

    @FXML
    private void clickExitButton(ActionEvent event) {
        System.exit(0);
    }
}
