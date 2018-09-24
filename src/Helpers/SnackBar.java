/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Edu
 */
public class SnackBar {
    public static void createSnackBar(String msg,StackPane panel){
            JFXSnackbar bar = new JFXSnackbar(panel);
            bar.setScaleX(2);
            bar.setScaleY(2);
            bar.setTranslateY(-100);
            bar.show(msg, 5000);
    }
}
