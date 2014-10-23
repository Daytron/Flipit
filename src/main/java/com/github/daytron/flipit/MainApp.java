package com.github.daytron.flipit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage stage;
    private final String MAIN_MENU_FXML = "MainMenu.fxml";
    private final String GAME_MAIN_FXML = "";
    
    
    public MainApp(){
    }
    
    public MainApp getInstance() {
        return this;
    }

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
    
    public void clickNewGame() {
        
    }
    
    private void gotoMainMenu() {
        try {
            replaceScene(MAIN_MENU_FXML);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoGameMain() {
        try {
            replaceScene(GAME_MAIN_FXML);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Parent replaceScene(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxml));
        
        Scene scene = stage.getScene();
       
        
        if (scene == null) {
            scene = new Scene(root);
            
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        
         scene.getStylesheets().add("/styles/StyleMainMenu.css");
        
        stage.sizeToScene();
        
        return root;
    }

}
