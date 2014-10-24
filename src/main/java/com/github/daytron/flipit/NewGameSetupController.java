/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class NewGameSetupController implements Initializable {
     private MainApp app;
    
    @FXML
    private ListView<?> mapList;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button startButton;
    @FXML
    private Button cancelButton;
    
     
     public void setApp(MainApp application) {
        this.app = application;
    }
    
     
     
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.imagePreview.setImage(new Image("http://isc.stuorg.iastate.edu/wp-content/uploads/sample.jpg"));
        
    }

    @FXML
    private void startNewGame(ActionEvent event) {
    }

    @FXML
    private void cancelSetup(ActionEvent event) {
        this.app.viewMainMenu();
    }
    
}
