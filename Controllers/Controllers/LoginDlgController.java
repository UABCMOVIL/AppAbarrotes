/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Datos.ConsultasBD;
import Datos.Sesion;
import Datos.Usuario;
import Helpers.DialogExitButtonEnabler;
import Helpers.SnackBar;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Israel Guerrero
 */
public class LoginDlgController extends Observable implements  Initializable {
    
    private  JFXDialogLayout newUserDlgLayout;
    private StackPane newUserDlgPanel;
    private JFXDialog newUserDlg;
    private ImageView newUserDlgExitButton;
    
    
    @FXML
    private TextField txtEmailLogin;
    
    @FXML
    private TextField txtPassLogin;
    
    @FXML
    private StackPane sesionDlgRootPane;
    
    @FXML
    private JFXButton loginEntrarBtn;
    
    @FXML
    private JFXButton newUserLoginBtn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       loginEntrarBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
           if(!txtEmailLogin.getText().isEmpty()&&!txtPassLogin.getText().isEmpty()){
               Usuario user = ConsultasBD.getInstance().obtenerUsuario(txtEmailLogin.getText(), txtPassLogin.getText());
               if(user!=null){
                   System.out.println("Se encontro el usuario");
                   Sesion.getInstance().setCurrentUser(user);
               }else{
                   System.out.println("No se encontro ese usuario");
                   SnackBar.createSnackBar("No se encontro ese usuario",sesionDlgRootPane);
               }
           }else{
               System.out.println("Ingresa los dos datos");
               SnackBar.createSnackBar("Ingresa los dos datos",sesionDlgRootPane);
           }
       });
       newUserLoginBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
           newUserDlgLayout = new JFXDialogLayout();
           try {
               newUserDlgPanel = FXMLLoader.load(this.getClass().getResource("/Dialogs/newUserDlg.fxml"));
           } catch (IOException ex) {
               Logger.getLogger(LoginDlgController.class.getName()).log(Level.SEVERE, null, ex);
           }
           newUserDlgExitButton = DialogExitButtonEnabler.getExitDialogButton(newUserDlgPanel);
           newUserDlgExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, ex->{
               newUserDlg.close();
           });
           newUserDlgLayout.getChildren().add(newUserDlgPanel);
           newUserDlg = new JFXDialog(sesionDlgRootPane,newUserDlgLayout,JFXDialog.DialogTransition.CENTER);
           newUserDlg.setOverlayClose(false);
           newUserDlg.show();
       });
    }
}
