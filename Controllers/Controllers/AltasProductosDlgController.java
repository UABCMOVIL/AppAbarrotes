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
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Israel Guerrero
 */
public class AltasProductosDlgController extends Observable implements Initializable{
    
    @FXML
    private StackPane rootPane;
    
    @FXML
    private JFXButton agregarBtn;
    
    @FXML
    private ComboBox txtCodigo;
    
    @FXML
    private TextField txtPrecio;
    
    @FXML
    private TextField txtCantidad;
    
    @FXML
    private TextField txtNombre;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtCodigo.setEditable(true);
        txtCodigo.setItems(ConsultasBD.getInstance().obtenerCodigosInventarioProductos());
        txtCodigo.valueProperty().addListener((e1,e2,e3)->{
            System.out.println(e3);
            Productos p = ConsultasBD.getInstance().obtenerProducto(e3.toString());
            if(p.getNombre()!=null){
                txtNombre.setText(p.getNombre());
                txtNombre.setEditable(false);
                txtCantidad.setText(String.valueOf(p.getCantidad()));
                txtPrecio.setText(String.valueOf(p.getPrecio()));
                txtPrecio.setEditable(false);
            }else{
                txtNombre.setEditable(true);
                txtPrecio.setEditable(true);
                txtNombre.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
            }
        });
        agregarBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            if(!txtNombre.getText().isEmpty()&&!txtCantidad.getText().isEmpty()&&!txtPrecio.getText().isEmpty()&&!txtCodigo.getSelectionModel().getSelectedItem().toString().isEmpty()){
                String exists;
                if(ConsultasBD.getInstance().obtenerCodigosInventarioProductos().contains(Integer.parseInt(txtCodigo.getSelectionModel().getSelectedItem().toString()))){
                    exists = "true";
                }else{
                    exists = "false";
                }
                DataHolder.getInstance().setData(new String[]{exists,txtCodigo.getSelectionModel().getSelectedItem().toString(),txtNombre.getText(),txtPrecio.getText(),txtCantidad.getText()}, "Alta Completada");
            }else{
                SnackBar.createSnackBar("Asegurate de llenar todos los campos", rootPane);
            }
        });
    }
    
}
