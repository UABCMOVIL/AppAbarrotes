/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoabarrotes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Edu
 */
public class ProyectoAbarrotes extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(false);
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/Scenes/MainScene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/style.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
