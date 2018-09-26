/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Datos.Sesion;
import Helpers.DialogExitButtonEnabler;
import Helpers.ScreenTransition;
import Helpers.SnackBar;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Israel Guerrero
 */
public class MainSceneController implements Initializable, Observer {
    private  JFXDialogLayout initSesionDlgLayout;
    private StackPane initSesionDlgPanel;
    private JFXDialog initSesionDlg;
    private ImageView initSesionDlgExitButton;
   
    @FXML
    private AnchorPane mainMenuAnchorPanel;
    
    @FXML
    private JFXButton btnCompras;
    
    @FXML
    private HBox menuHBox;
    
    @FXML
    private JFXButton btnProductos;
    
    @FXML
    private Label msgBienvenida;
    
    @FXML
    private Label label;
    
    @FXML
    private StackPane RootPane;
    
    @FXML
    private JFXButton initSesionBtn;
    
    @FXML
    private ImageView img;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            if(Sesion.getInstance().getCurrentUser()==null){
                menuHBox.getChildren().remove(btnProductos);
                menuHBox.getChildren().remove(btnCompras);
                initSesionBtn.setText("Iniciar Sesion");
            }else{
                initSesionBtn.setText("Cerrar Sesion");
                if(Sesion.getInstance().getCurrentUser().getRol().equals("Cajero")){
                     menuHBox.getChildren().remove(btnProductos);
                }
            }
            if(btnProductos.isVisible()){
                btnProductos.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
                    Parent newScene = null;
                    try {
                        newScene = FXMLLoader.load(this.getClass().getResource("/Scenes/ProductosScene.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ScreenTransition.transicionPantalla(RootPane, newScene);
                });
            }
            if(btnCompras.isVisible()){
                btnCompras.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
                    Parent newScene = null;
                    try{
                        newScene = FXMLLoader.load(getClass().getResource("/Scenes/ComprasScene.fxml"));
                    }catch(Exception ex){
                        Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ScreenTransition.transicionPantalla(RootPane, newScene);
                });
            }
            initSesionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
              switch(initSesionBtn.getText()){
                case "Iniciar Sesion":{
                        initSesionDlgLayout = new JFXDialogLayout();
                        try {
                            initSesionDlgPanel = FXMLLoader.load(this.getClass().getResource("/Dialogs/LoginDlg.fxml"));
                        } catch (IOException ex) {
                            Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        initSesionDlgExitButton = DialogExitButtonEnabler.getExitDialogButton(initSesionDlgPanel);
                        initSesionDlgExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ex->{
                            initSesionDlg.close();
                        });                   
                        initSesionDlgLayout.getChildren().add(initSesionDlgPanel);
                        initSesionDlg = new JFXDialog(RootPane,initSesionDlgLayout,JFXDialog.DialogTransition.CENTER);
                        initSesionDlg.setOverlayClose(false);
                        initSesionDlg.show();
                        break;
                    }
                    case "Cerrar Sesion":{
                        System.out.println("Cerraste sesion");
                        menuHBox.getChildren().remove(btnProductos);
                        menuHBox.getChildren().remove(btnCompras);
                        initSesionBtn.setText("Iniciar Sesion");
                        Sesion.getInstance().setCurrentUser(null);
                        break;
                    }
                }
            });
            Sesion.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(Sesion.getInstance().getCurrentUser()!=null){
            //si entra aqui es que se inicio sesion
            initSesionDlg.close();
            menuHBox.getChildren().remove(initSesionBtn);
            switch(Sesion.getInstance().getCurrentUser().getRol()){
              case "Cajero":{
                 menuHBox.getChildren().add(btnCompras);
                 break;
              }
              case "Gerente":{
                  menuHBox.getChildren().add(btnProductos);
                  menuHBox.getChildren().add(btnCompras);
                  break;
             }
          }
          menuHBox.getChildren().add(initSesionBtn);
          msgBienvenida.setText("Bienvenido de nuevo, "+Sesion.getInstance().getCurrentUser().getNombre()+".");
          initSesionBtn.setText("Cerrar Sesion");
          SnackBar.createSnackBar("Sesion Iniciada",RootPane);
       }else{
                  SnackBar.createSnackBar("Sesion Cerrada", RootPane);
                  msgBienvenida.setText("Bienvenido, para comenzar a usar el sistema por favor inicia sesion.");
                  initSesionBtn.setText("Iniciar Sesion");
                }
    }
}
