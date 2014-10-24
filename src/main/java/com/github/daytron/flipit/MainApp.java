package com.github.daytron.flipit;

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
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;
    private final String MAIN_MENU_FXML = "MainMenu.fxml";
    private final String NEW_GAME_SETUP_FXML = "NewGameSetup.fxml";
    private final String GAME_MAIN_FXML = "";

    @Override
    public void start(Stage primary_stage) throws Exception {
        stage = primary_stage;
        gotoMainMenu();
        primary_stage.show();

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
            MainMenuController menuCtrl = 
                    (MainMenuController) replaceScene(MAIN_MENU_FXML);
            menuCtrl.setApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoNewGameSetup() {
        try {
            NewGameSetupController menuCtrl = 
                    (NewGameSetupController) replaceScene(NEW_GAME_SETUP_FXML);
            menuCtrl.setApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoGameMain() {

    }

    private Initializable replaceScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainApp.class.getResourceAsStream("/fxml/" + fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainApp.class.getResource("/fxml/" + fxml));

        Parent pane;
        
            try {
                pane = loader.load(in);
            } finally {
                in.close();
            }
            Scene scene = new Scene(pane);
            stage.setScene(scene);
        

        
        

        stage.sizeToScene();

        return (Initializable) loader.getController();
    }

}
