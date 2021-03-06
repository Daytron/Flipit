/* 
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera.
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

import com.github.daytron.flipit.MainApp;
import com.github.daytron.flipit.data.DialogMessage;
import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.data.DialogType;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
        Dialog dialog = new Dialog(
                DialogType.CONFIRMATION,
                DialogMessage.CONFIRMATION_HEAD_TITLE.getText(),
                DialogMessage.CONFIRMATION_TITLE.getText(),
                DialogMessage.CONFIRM_EXIT.getText());

        dialog.showAndWait();

        if (dialog.getResponse() == DialogResponse.YES) {
            Platform.exit();
        }
    }
}
