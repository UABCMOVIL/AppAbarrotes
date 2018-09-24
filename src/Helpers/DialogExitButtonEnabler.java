/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Edu
 */
public class DialogExitButtonEnabler {
    //Esta clase regresa el ImageView que tiene la imagen de x que es usada para cerrar los dialogos.
    public static ImageView getExitDialogButton(StackPane panel){
        for(Node node : panel.getChildren()){
                if(node instanceof BorderPane){
                    for(Node nodeInsideBorderPane : ((BorderPane)node).getChildren()){
                        if(nodeInsideBorderPane instanceof HBox){
                            for(Node nodeInsideHBox : ((HBox)nodeInsideBorderPane).getChildren()){
                                if(nodeInsideHBox instanceof ImageView){
                                    System.out.println("ENTRO AQUi");
                                    return (ImageView)nodeInsideHBox;
                                }
                            }
                        }
                    }
                }
        }
        return null;
    }
}
