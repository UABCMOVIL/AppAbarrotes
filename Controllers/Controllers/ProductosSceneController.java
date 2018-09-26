/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Datos.ConsultasBD;
import Datos.DataHolder;
import Datos.Productos;
import Helpers.DialogExitButtonEnabler;
import Helpers.ScreenTransition;
import Helpers.SnackBar;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Israel Guerrero
 */
public class ProductosSceneController implements Initializable, Observer{
    
    private JFXDialogLayout altasDlgLayout;
    private JFXDialog altasDlg;
    private StackPane altasDlgPane;
    private ImageView altasDlgExitButton;
    
    private JFXDialogLayout bajasDlgLayout;
    private JFXDialog bajasDlg;
    private StackPane bajasDlgPane;
    private ImageView bajasDlgExitButton;
    
    private TableView<Productos> tabla;
    
    @FXML
    private StackPane RootPane;
    
    @FXML
    private AnchorPane CenterPanel;
    
    @FXML
    private JFXButton altasBtn;
    
    @FXML
    private JFXButton bajasBtn;
    
    @FXML
    private JFXButton btnPrincipal;
    
    @FXML
    private JFXButton btnCompras;
    
    @FXML
    private JFXButton SesionBtn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnPrincipal.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            Parent nextScene = null;
            try {
                nextScene = FXMLLoader.load(getClass().getResource("/Scenes/MainScene.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(ProductosSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ScreenTransition.transicionPantalla(RootPane, nextScene);
        });
        createTable(ConsultasBD.getInstance().obtenerInventarioProductos());
        CenterPanel.getChildren().add(tabla);
        altasBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            altasDlgLayout = new JFXDialogLayout();
            try {
                altasDlgPane = FXMLLoader.load(this.getClass().getResource("/Dialogs/altasProductos.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(ProductosSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            altasDlgExitButton = DialogExitButtonEnabler.getExitDialogButton(altasDlgPane);
            altasDlgExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,ex->{
                altasDlg.close();
            });
            altasDlgLayout.getChildren().add(altasDlgPane);
            altasDlg = new JFXDialog(RootPane,altasDlgLayout,JFXDialog.DialogTransition.CENTER);
            altasDlg.show();
        });
        bajasBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            bajasDlgLayout = new JFXDialogLayout();
            try {
                bajasDlgPane = FXMLLoader.load(this.getClass().getResource("/Dialogs/bajasProductos.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(ProductosSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            bajasDlgExitButton = DialogExitButtonEnabler.getExitDialogButton(bajasDlgPane);
            bajasDlgExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,ex->{
                bajasDlg.close();
            });
            bajasDlgLayout.getChildren().add(bajasDlgPane);
            bajasDlg = new JFXDialog(RootPane,bajasDlgLayout,JFXDialog.DialogTransition.CENTER);
            bajasDlg.show();
        });
        DataHolder.getInstance().addObserver(this);
    }
    
    private void createTable(ObservableList<Productos> response) {
            TableColumn<Productos,Integer> id = new TableColumn<>("ID");
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            TableColumn<Productos,String> nombre = new TableColumn<>("Nombre");
            nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            TableColumn<Productos,Double> precio = new TableColumn<>("Precio Individual");
            precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            TableColumn<Productos,Integer> codigo = new TableColumn<>("Codigo");
            codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            TableColumn<Productos,Integer> cantidad = new TableColumn<>("Stock");
            cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            tabla = new TableView<>();
            tabla.setItems(response);
            id.setPrefWidth(CenterPanel.getPrefWidth()/5);
            id.setStyle( "-fx-alignment: CENTER;");
            nombre.setPrefWidth(CenterPanel.getPrefWidth()/5);
            nombre.setStyle( "-fx-alignment: CENTER;");
            precio.setPrefWidth(CenterPanel.getPrefWidth()/5);
            precio.setStyle( "-fx-alignment: CENTER;");
            codigo.setPrefWidth(CenterPanel.getPrefWidth()/5);
            codigo.setStyle( "-fx-alignment: CENTER;");
            cantidad.setPrefWidth(CenterPanel.getPrefWidth()/5);
            cantidad.setStyle( "-fx-alignment: CENTER;");
            tabla.getColumns().addAll(id,nombre,precio,codigo,cantidad);
            tabla.setPrefSize(CenterPanel.getPrefWidth(),CenterPanel.getPrefHeight());
    }

    @Override
    public void update(Observable o, Object arg) {
        switch((String)arg){
            case "Alta Completada":{
                List<String> datosObtenidos = DataHolder.getInstance().getData();
                altasDlg.close();
                Productos p = new Productos();
                p.setCodigo(Integer.parseInt(datosObtenidos.get(1)));
                p.setNombre(datosObtenidos.get(2));
                p.setId(0);
                p.setPrecio(Double.parseDouble(datosObtenidos.get(3)));
                p.setCantidad(Integer.parseInt(datosObtenidos.get(4)));
                boolean success = false;
                //Si ya existe el registro entonces solo se actualiza la cantidad
                if(datosObtenidos.get(0).equals("true")){
                    success = ConsultasBD.getInstance().actualizarProducto(datosObtenidos.get(4), datosObtenidos.get(1));
                }else{
                    success = ConsultasBD.getInstance().agregarNuevoProducto(p);
                }
                if(success){
                    SnackBar.createSnackBar("Se Agrego el producto exitosamente!", RootPane);
                    //se recarga la tabla con los nuevos datos
                    createTable(ConsultasBD.getInstance().obtenerInventarioProductos());
                    CenterPanel.getChildren().add(tabla);
                }else{
                    SnackBar.createSnackBar("Hubo un error con la BD!", RootPane);
                }
                break;
            }
            case "Baja Completada":{
                List<String> datosObtenidos = DataHolder.getInstance().getData();
                bajasDlg.close();
                if(ConsultasBD.getInstance().actualizarProducto(datosObtenidos.get(1), datosObtenidos.get(0))){
                    SnackBar.createSnackBar("Se actualizo el registro exitosamente!", RootPane);
                    //se recarga la tabla con los nuevos datos
                    createTable(ConsultasBD.getInstance().obtenerInventarioProductos());
                    CenterPanel.getChildren().add(tabla);
                }else{
                    SnackBar.createSnackBar("Hubo un error con la BD!", RootPane);
                }
                break;
            }
        }
    }
    
}
