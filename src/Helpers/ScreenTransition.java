/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Edu
 */
public class ScreenTransition {
    public static void transicionPantalla(Scene old,Scene next){
       FadeTransition anim = new FadeTransition();
        anim.setNode(old.getRoot());
        anim.setDuration(new Duration(1000));
        anim.setFromValue(1.0);
        anim.setToValue(0.0);
        anim.setCycleCount(1);
        anim.play();
        Stage window = (Stage)old.getWindow();  
        anim.setOnFinished(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                window.setScene(next);
                window.show();
           }
        });  
    }
    
    public static void transicionPantalla(StackPane root,Parent nextScene){
        Scene newScene = new Scene(nextScene,Helpers.ScreenSize.Width,Helpers.ScreenSize.Height);
        newScene.getStylesheets().add("/css/style.css");
        FadeTransition anim = new FadeTransition();
        anim.setNode(root);
        anim.setDuration(new Duration(1000));
        anim.setFromValue(1.0);
        anim.setToValue(0.0);
        anim.setCycleCount(1);
        anim.play();
        Stage window = (Stage)root.getScene().getWindow();  
        anim.setOnFinished(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                window.setScene(newScene);
                window.show();
           }
        });
    }
}
