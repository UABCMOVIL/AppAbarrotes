/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Datos.ConsultasBD;
import Datos.DataHolder;
import Datos.Productos;
import Helpers.SnackBar;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Israel Guerrero
 */
public class BajasProductosDlgController implements Initializable {

    private int cantidadTotalActual;
    
    @FXML
    private StackPane rootPane;
    
    @FXML
    private ChoiceBox txtCodigo;
    
    @FXML
    private Label txtCantidad;
    
    @FXML
    private Label txtNombre;
    
    @FXML
    private Label txtPrecio;
    
    @FXML
    private JFXButton eliminarBtn;
    
    @FXML
    private TextField txtEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtCodigo.setItems(ConsultasBD.getInstance().obtenerCodigosInventarioProductos());
        txtCodigo.valueProperty().addListener((e1,e2,e3)->{
            Productos p = ConsultasBD.getInstance().obtenerProducto(e3.toString());
            if(p.getNombre()!=null){
                cantidadTotalActual = p.getCantidad();
                txtCantidad.setText(String.valueOf(p.getCantidad()));
                txtNombre.setText(p.getNombre());
                txtPrecio.setText(String.valueOf(p.getPrecio()));
            }else{
                cantidadTotalActual = -1;
                txtCantidad.setText("");
                txtNombre.setText("");
                txtPrecio.setText("");
            }
        });
        eliminarBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            if(!txtEliminar.getText().isEmpty()){
                if(cantidadTotalActual!=-1){
                     try{
                        if(Integer.parseInt(txtEliminar.getText())<= cantidadTotalActual){
                              DataHolder.getInstance().setData(new String[]{txtCodigo.getSelectionModel().getSelectedItem().toString(),String.valueOf(Integer.parseInt(txtCantidad.getText())-Integer.parseInt(txtEliminar.getText()))}, "Baja Completada");
                        }else{
                            SnackBar.createSnackBar("La cantidad es menor a la que deseas eliminar, por favor modifica la cantidad a eliminar.", rootPane);
                        }
                    }catch(Exception ex){
                        SnackBar.createSnackBar("La cantidad debe ser con solo numeros.", rootPane);
                    }
                }else{
                    SnackBar.createSnackBar("Asegurate de seleccionar un producto por su codigo.", rootPane);
                }
            }else{
                SnackBar.createSnackBar("Asegurate de ingresar la cantidad que deseas eliminar.", rootPane);
            }
        });
    }
    
}
