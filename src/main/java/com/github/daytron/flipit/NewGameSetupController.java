/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ListView<String> mapList;
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
     * Initialises the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //this.imagePreview.setImage(new Image("http://isc.stuorg.iastate.edu/wp-content/uploads/sample.jpg"));

    }

    public void loadMapNames() {
        // Extract map names from preloader event
        List<String> listMapNames = this.app.getGamePreloader().getMapNames();
        
        // Convert the list to a new readable list, ObservableList for listview
        ObservableList<String> listOfMapNames
                = FXCollections.observableArrayList(listMapNames);

        // Update the map name list
        this.mapList.setItems(listOfMapNames);

        // Add an listener for item selection
        this.mapList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                System.out.println("Selected item: " + newValue);
                
                
                // Get the map selected by default
                app.getGamePreloader().setMapSelected(mapList.getSelectionModel().getSelectedItem());
                
                imagePreview.setImage(new Image(app.getGamePreloader().getMapPreviewImageSelected()));
            }
        });
        
        
    }


    @FXML
    private void clickCancelSetup(ActionEvent event) {
        this.app.viewMainMenu();
    }

    @FXML
    private void clickStart(ActionEvent event) {
        // Load the main game frame
        this.app.viewMainGame();
    }

    

    
    

}
