/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Datos.ConsultasBD;
import Datos.Productos;
import Datos.Recibo;
import Helpers.SnackBar;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Israel Guerrero
 */
public class ComprasSceneController implements Initializable {

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    
    
    private JFXDialogLayout reciboDlgLayout;
    private JFXDialog reciboDlg;
    private ScrollPane reciboDlgPane;
    
    private Map<Integer,Integer> cantidades = new HashMap<>();
    private Productos productoActual;
    private List<Recibo> recibos = new LinkedList<>();
    
    @FXML
    private AnchorPane facturasPane;
    
    @FXML
    private StackPane rootPane;
    
    @FXML
    private VBox lista;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblCantidad;
    @FXML
    private Label lblStock;
    @FXML
    private TextField txtCantidad;
    @FXML
    private Label txtNombre;
    @FXML
    private Label txtPrecio;
    @FXML
    private Label txtStock;
    @FXML
    private JFXButton btnAgregar;
    @FXML
    private JFXButton btnFacturar;
    @FXML
    private ChoiceBox txtCodigo;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Integer c : ConsultasBD.getInstance().obtenerCodigosInventarioProductos() ){
            cantidades.put(c, 0);
        }
        txtCodigo.setItems(ConsultasBD.getInstance().obtenerCodigosInventarioProductos());
        txtCodigo.valueProperty().addListener((e1,e2,e3)->{
            Productos p = ConsultasBD.getInstance().obtenerProducto(e3.toString());
            if(p.getNombre()!=null){
                lblStock.setVisible(true);
                txtStock.setVisible(true);
                txtStock.setText(String.valueOf(p.getCantidad()));
                lblNombre.setVisible(true);
                txtNombre.setVisible(true);
                txtNombre.setText(p.getNombre());
                lblPrecio.setVisible(true);
                txtPrecio.setVisible(true);
                txtPrecio.setText(String.valueOf(p.getPrecio()));
                btnAgregar.setVisible(true);
                txtCantidad.setVisible(true);
                lblCantidad.setVisible(true);
                productoActual = p;
            }
        });
        btnAgregar.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            System.out.println("codigo actual : "+productoActual.getCodigo()+"  cantidadActual : "+cantidades.get(productoActual.getCodigo()));
            if(cantidades.get(productoActual.getCodigo())+Integer.parseInt(txtCantidad.getText()) <= productoActual.getCantidad()){
                cantidades.put(productoActual.getCodigo(), cantidades.get(productoActual.getCodigo())+Integer.parseInt(txtCantidad.getText()));
                try {
                    VBox nuevo = FXMLLoader.load(getClass().getResource("/Helpers/CustomEtiqueta.fxml"));
                    nuevo.setOnDragDetected(e1->{
                        System.out.println("Event on Source: drag detected");
                        orgSceneX = e1.getSceneX();
                        orgSceneY = e1.getSceneY();
                        orgTranslateX = ((VBox)e1.getSource()).getTranslateX();
                        orgTranslateY = ((VBox)e1.getSource()).getTranslateY();
                        Dragboard db = ((VBox)e1.getSource()).startDragAndDrop(TransferMode.ANY);
                        ClipboardContent content = new ClipboardContent();
                        content.putString(e1.getSource().toString());
                        db.setContent(content);
                    });
                    nuevo.setOnDragOver(e1->{
                        double offsetX = e1.getSceneX() - orgSceneX;
                        double offsetY = e1.getSceneY() - orgSceneY;
                        double newTranslateX = orgTranslateX + offsetX;
                        double newTranslateY = orgTranslateY + offsetY;
                        ((VBox)e1.getSource()).setTranslateX(newTranslateX);
                        ((VBox)e1.getSource()).setTranslateY(newTranslateY);
                        if(((VBox)e1.getSource()).getTranslateX()>=80){
                            ((VBox)e1.getSource()).setDisable(true);
                            ((VBox)e1.getSource()).setTranslateX(0);
                            ((VBox)e1.getSource()).setTranslateY(0);
                            for(Node n : ((VBox)e1.getSource()).getChildren()){
                                if(n instanceof Label){
                                    ((Label)n).setVisible(true);
                                }
                            }
                        }
                     });
                    setEtiqueta(nuevo);
                    lista.getChildren().add(nuevo);
                } catch (IOException ex) {
                    Logger.getLogger(ComprasSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                SnackBar.createSnackBar("La cantidad de este producto no es suficiente, selecciona una menor cantidad", rootPane);
            }
        });
        btnFacturar.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            recibos.clear();
            for(Node n:lista.getChildren()){
                   if(!((VBox)n).isDisabled()){
                       recibos.add(getReciboFromEtiqueta((VBox)n));
                   }
            }
            reciboDlgLayout = new JFXDialogLayout();
            try {
                reciboDlgPane = FXMLLoader.load(getClass().getResource("/Dialogs/ReciboDlg.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(ComprasSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            //listaProductos.getChildren().clear();
            for(Recibo n : recibos){
                addToReciboDlg(setReciboEntry(n),reciboDlgPane);
            }
            reciboDlgLayout.getChildren().add(reciboDlgPane);
            reciboDlg = new JFXDialog(rootPane,reciboDlgLayout,JFXDialog.DialogTransition.CENTER);
            reciboDlg.show();
        });
    }
    
    private AnchorPane setReciboEntry(Recibo r){
        AnchorPane pane = new AnchorPane();
        try {
            pane = FXMLLoader.load(getClass().getResource("/Helpers/productoModel.fxml"));
            for(Node n : pane.getChildren()){
                switch(n.getId()){
                    case "txtProducto":((Label)n).setText(r.getNombre());break;
                    case "txtCantidad":((Label)n).setText(String.valueOf(r.getCant()));break;
                    case "txtPrecio":((Label)n).setText(String.valueOf(r.getPrecio()));break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ComprasSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pane;
    }
    
    private Recibo getReciboFromEtiqueta(VBox v){
        Recibo r = new Recibo();
        for(Node node: v.getChildren()){
            if(node instanceof HBox){
                for(Node n : ((HBox)node).getChildren()){
                    if(n.getId()!=null){
                        switch(n.getId()){
                                case "producto":r.setNombre(((Label)n).getText().toString());break;
                                case "cantidad":r.setCant(Integer.parseInt(((Label)n).getText().toString()));break;
                                case "precio":r.setPrecio(Double.parseDouble(((Label)n).getText().toString()));break;
                        }
                    } 
                }
            }
        }
        return r;
    }
    
    private void setEtiqueta(VBox etiqueta){
        for(Node node: etiqueta.getChildren()){
            if(node instanceof HBox){
                for(Node n : ((HBox)node).getChildren()){
                    if(n.getId()!=null){
                        switch(n.getId()){
                                case "producto":((Label)n).setText(productoActual.getNombre());break;
                                case "cantidad":((Label)n).setText(txtCantidad.getText());break;
                                case "precio":((Label)n).setText(String.valueOf(productoActual.getPrecio()*Integer.parseInt(txtCantidad.getText())));break;
                                case "codigo":((Label)n).setText(String.valueOf(productoActual.getCodigo()));break;
                        }
                    } 
                }
            }
        }
    }

    private void addToReciboDlg(AnchorPane setReciboEntry,ScrollPane pane) {
          double precio = 0;
          for(Node n : setReciboEntry.getChildren()){
              if(n instanceof Label){
                 if(n.getId()!=null&&n.getId().equals("txtPrecio")){
                    precio = Double.parseDouble(((Label)n).getText().toString());
                 }
              }
          }
          if(pane.getContent() instanceof AnchorPane){
            for(Node n2 : ((AnchorPane)pane.getContent()).getChildren()){
                if(n2 instanceof VBox){
                    for(Node n3 : ((VBox)n2).getChildren()){
                      if(n3.getId()!=null&&n3.getId().equals("listaProductos")){
                          ((VBox)n3).getChildren().add(setReciboEntry);
                      }
                      if(n3.getId()!=null&&n3.getId().equals("costoPane")){
                          for(Node n4 : ((AnchorPane)n3).getChildren()){
                              if(n4.getId()!=null&&n4.getId().equals("txtCostoTotal")){
                                  ((Label)n4).setText(String.valueOf(Double.parseDouble(((Label)n4).getText().toString())+precio));
                              }
                          }
                      }
                    }
                }
             }
          }  
       }
}
