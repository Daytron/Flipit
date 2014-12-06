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

import com.github.daytron.flipit.utility.GlobalSettings;
import com.github.daytron.flipit.MainApp;
import com.github.daytron.flipit.data.PlayerType;
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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author ryan
 */
public class NewGameSetupController implements Initializable {

    private MainApp app;
    private final Rectangle blueRectangle = new Rectangle(60, 20,
            Color.web(GlobalSettings.PLAYER_COLOR_BLUE));
    private final Rectangle redRectangle = new Rectangle(60, 20,
            Color.web(GlobalSettings.PLAYER_COLOR_RED));

    @FXML
    private ListView<String> mapList;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button startButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label startPositionP1Label;
    @FXML
    private Label colorP1Label;
    @FXML
    private ComboBox<String> startPositionP1ComboBox;
    @FXML
    private ComboBox<String> colorP1ComboBox;
    @FXML
    private Label startPositionP2Label;
    @FXML
    private ComboBox<String> startPositionP2ComboBox;
    @FXML
    private Label colorP2Label;
    @FXML
    private ComboBox<String> colorP2ComboBox;
    @FXML
    private AnchorPane preview_anchor_pane;

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

        this.preview_anchor_pane.setBackground(new Background(
            new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // Define list items
        ObservableList<String> playerOptions
                = FXCollections.observableArrayList(
                        "Human",
                        "Computer"
                );
        // Populate with items
        this.startPositionP1ComboBox.setItems(playerOptions);
        this.startPositionP2ComboBox.setItems(playerOptions);

        // Apply default values
        this.startPositionP1ComboBox.getSelectionModel().select(GlobalSettings.PLAYER_OPTION_HUMAN);
        this.startPositionP2ComboBox.getSelectionModel().select(GlobalSettings.PLAYER_OPTION_COMPUTER);

        // Add player 1 combobox listener
        this.startPositionP1ComboBox.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                app.getGamePreloader().setPlayer1(PlayerType.valueOf(
                    newValue.toUpperCase()));
                
                if (newValue.contains(GlobalSettings.PLAYER_OPTION_HUMAN)) {
                    startPositionP2ComboBox.getSelectionModel()
                            .select(GlobalSettings.PLAYER_OPTION_COMPUTER);
                    app.getGamePreloader().setPlayer2(PlayerType.COMPUTER);
                } else {
                    startPositionP2ComboBox.getSelectionModel()
                            .select(GlobalSettings.PLAYER_OPTION_HUMAN);
                    app.getGamePreloader().setPlayer2(PlayerType.HUMAN);
                }
            }
        });
        
        // Add player 2 combobox listener
        this.startPositionP2ComboBox.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                app.getGamePreloader().setPlayer2(PlayerType.valueOf(
                    newValue.toUpperCase()));
                
                if (newValue.contains(GlobalSettings.PLAYER_OPTION_HUMAN)) {
                    startPositionP1ComboBox.getSelectionModel()
                            .select(GlobalSettings.PLAYER_OPTION_COMPUTER);
                    app.getGamePreloader().setPlayer1(PlayerType.COMPUTER);
                } else {
                    startPositionP1ComboBox
                            .getSelectionModel().select(GlobalSettings.PLAYER_OPTION_HUMAN);
                    app.getGamePreloader().setPlayer2(PlayerType.HUMAN);
                }
            }
        });

        // ============== COLOR AREA ================= //
        ObservableList<String> playerColorOptions = FXCollections.observableArrayList(GlobalSettings.PLAYER_COLOR_BLUE, GlobalSettings.PLAYER_COLOR_RED);

        this.colorP1ComboBox.setItems(playerColorOptions);
        this.colorP2ComboBox.setItems(playerColorOptions);
        /*
         this.colorP1ComboBox.getSelectionModel().select(blueRectangle);
         this.colorP2ComboBox.getSelectionModel().select(redRectangle);
         */

        Callback<ListView<String>, ListCell<String>> factory = new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ColorRectCell();

            }
        };

        this.colorP1ComboBox.setCellFactory(factory);
        this.colorP1ComboBox.setButtonCell(factory.call(null));
        this.colorP1ComboBox.setValue(GlobalSettings.PLAYER_COLOR_BLUE);

        this.colorP2ComboBox.setCellFactory(factory);
        this.colorP2ComboBox.setButtonCell(factory.call(null));
        this.colorP2ComboBox.setValue(GlobalSettings.PLAYER_COLOR_RED);
        
        // Player 1 color combobox listener
        this.colorP1ComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                colorP1ComboBox.setValue(newValue);
                if (newValue.contains(GlobalSettings.PLAYER_COLOR_BLUE)) {
                    colorP2ComboBox.setValue(GlobalSettings.PLAYER_COLOR_RED);
                } else {
                    colorP2ComboBox.setValue(GlobalSettings.PLAYER_COLOR_BLUE);
                }
            }
        });
        
        // Player 2 color combobox listener
        this.colorP2ComboBox.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                colorP2ComboBox.setValue(newValue);
                
                if (newValue.contains(GlobalSettings.PLAYER_COLOR_BLUE)) {
                    colorP1ComboBox.setValue(GlobalSettings.PLAYER_COLOR_RED);
                } else {
                    colorP1ComboBox.setValue(GlobalSettings.PLAYER_COLOR_BLUE);
                }
            }
        });

    }

    public void loadDefaultValuesToPreloader() {
        // Set default player start position values to gamePreloader
        this.app.getGamePreloader().setPlayer1(PlayerType.HUMAN);
        this.app.getGamePreloader().setPlayer2(PlayerType.COMPUTER);

        // Set default player color values to gamepreloader
        this.app.getGamePreloader().setPlayer1ColorSelected(GlobalSettings.PLAYER_COLOR_BLUE);
        this.app.getGamePreloader().setPlayer2ColorSelected(GlobalSettings.PLAYER_COLOR_RED);
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

                // Get the map selected by default
                app.getGamePreloader().setMapSelected(mapList.getSelectionModel().getSelectedItem());

                // Update the map preview image
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
        if (this.colorP1ComboBox.getValue().equals(this.colorP2ComboBox.getValue())) {
            System.out.println("please use separate colors");
        } else {
            this.app.getGamePreloader().setPlayer1ColorSelected(this.colorP1ComboBox.getValue());
            this.app.getGamePreloader().setPlayer2ColorSelected(this.colorP2ComboBox.getValue());
            // Load the main game frame
            this.app.viewMainGame();
        }
        
        
    }

}

class ColorRectCell extends ListCell<String> {

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        Rectangle rect1 = new Rectangle(60, 20);
        if (item != null) {
            rect1.setFill(Color.web(item));
            setGraphic(rect1);
            
            
        }

    }
}
