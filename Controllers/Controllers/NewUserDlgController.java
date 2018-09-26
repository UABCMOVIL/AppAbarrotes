/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Datos.ConsultasBD;
import Datos.Usuario;
import Helpers.SnackBar;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Israel Guerrero
 */
public class NewUserDlgController implements Initializable {

    @FXML
    private TextField txtNewUsuarioEmail;
    
    @FXML
    private StackPane newUserRootPaneButton;
    
    @FXML
    private Label errorCreacionUsuarioLabel;
    
    @FXML
    private TextField txtPassNewUser;
    @FXML
    private TextField txtNombreNewUser;
    @FXML
    private TextField txtApellidoNewUser;
    
    @FXML
    private ChoiceBox rolChoiceBox;
    
    @FXML
    private JFXButton newUserBtn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       rolChoiceBox.setItems(FXCollections.observableArrayList("Cajero","Gerente"));
       newUserBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
           if(!txtNewUsuarioEmail.getText().isEmpty()&&!txtPassNewUser.getText().isEmpty()
                   &&!txtNombreNewUser.getText().isEmpty()&&!txtApellidoNewUser.getText().isEmpty()){
              if(ConsultasBD.getInstance().agregarNuevoUsuario(new Usuario(txtNombreNewUser.getText(),txtApellidoNewUser.getText(),
              txtPassNewUser.getText(),rolChoiceBox.getSelectionModel().getSelectedItem().toString(),txtNewUsuarioEmail.getText()))){
                  SnackBar.createSnackBar("Se agrego el usuario",newUserRootPaneButton);
              }else{
                  SnackBar.createSnackBar("Hubo un error con la base de datos y no se agrego",newUserRootPaneButton);
              }
           }else{
               SnackBar.createSnackBar("Asegurate de ingresar todos los campos solicitados",newUserRootPaneButton);
           }
       });
    }
}
