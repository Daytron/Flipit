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
package com.github.daytron.flipit;

import com.github.daytron.flipit.controller.NewGameSetupController;
import com.github.daytron.flipit.controller.MainMenuController;
import com.github.daytron.flipit.controller.GameController;
import com.github.daytron.flipit.data.DialogMessage;
import com.github.daytron.flipit.data.Fxml;
import com.github.daytron.flipit.data.ImageProperty;
import com.github.daytron.flipit.dialog.ErrorDialog;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;
    private final String MAIN_MENU_FXML =  Fxml.MAIN_MENU.getFxml();
    private final String NEW_GAME_SETUP_FXML = Fxml.NEW_GAME_SETUP.getFxml();
    private final String GAME_MAIN_FXML = Fxml.GAME_MAIN.getFxml();
    private GamePreloader preLoader;

    @Override
    public void start(Stage primary_stage) throws Exception {
        this.preLoader = new GamePreloader();
        this.preLoader.init();

        this.stage = primary_stage;
        gotoMainMenu();
        
        stage.getIcons().add(
                new Image(MainApp.class.getResourceAsStream(
                                ImageProperty.ICON.getPath())));
        
        primary_stage.show();

    }

    public GamePreloader getGamePreloader() {
        return this.preLoader;
    }

    public void showNoMapFoundDialog() {
        ErrorDialog dialog = new ErrorDialog(
                DialogMessage.ERROR_TITLE.getText(), 
                DialogMessage.ERROR_NO_MAPS_FOUND.getText());
        
        dialog.setTitle(DialogMessage.ERROR_HEAD_TITLE.getText());
        dialog.showAndWait();
    }

    public void viewNewGameSetup() {
        gotoNewGameSetup();
    }
    
    public void viewMainMenu() {
        gotoMainMenu();
    }

    public void viewMainGame() {
        gotoGameMain();
    }

    private void gotoMainMenu() {
        try {
            MainMenuController menuCtrl
                    = (MainMenuController) replaceScene(MAIN_MENU_FXML);
            menuCtrl.setApp(this);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoNewGameSetup() {
        try {
            NewGameSetupController newGameCtrl
                    = (NewGameSetupController) replaceScene(NEW_GAME_SETUP_FXML);
            newGameCtrl.setApp(this);
            newGameCtrl.loadMapNames();
            newGameCtrl.loadDefaultValuesToPreloader();

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoGameMain() {
        try {
            GameController mainGameCtrl
                    = (GameController) replaceScene(GAME_MAIN_FXML);
            mainGameCtrl.setApp(this);
            mainGameCtrl.run();
        } catch (Exception e) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Initializable replaceScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainApp.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainApp.class.getResource(fxml));

        Parent pane;

        try {
            pane = loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(pane);
        stage.setTitle("Flipit");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();

        return (Initializable) loader.getController();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
