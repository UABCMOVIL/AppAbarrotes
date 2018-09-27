/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Edu
 */
public class ConsultasBD {
    private Connection con;
    private ConexionBD bd;
    private String sql;
    private Statement query;
    private PreparedStatement pStatement;
    private ResultSet response;
    
    private static ConsultasBD instance = null;
    
    public static ConsultasBD getInstance(){
        if(instance == null){
            instance = new ConsultasBD();
        }
        return instance;
    }
    
    private ConsultasBD(){
        bd = new ConexionBD();
        con = bd.getConexion();
    }
    
    public Usuario obtenerUsuario(String usu, String pass){
        Usuario usuario = new Usuario();
        sql = "select * from Usuarios where email = '"+usu+"' and password = '"+pass+"'";
        try{
            query = con.createStatement();
            response = query.executeQuery(sql);
            while(response.next()){
                usuario.setId(response.getInt("id_usuario"));
                usuario.setRol(response.getString("rol"));
                usuario.setNombre(response.getString("nombre"));
                usuario.setApellido(response.getString("apellido"));
                usuario.setPass(response.getString("password"));
                usuario.setEmail(response.getString("email"));
             }
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
        if(usuario.getEmail() == null){
            return null;
        }else{
            return usuario;
        }
    }
    
   public boolean agregarNuevoUsuario(Usuario usu){
       boolean result = false;
       sql = "INSERT INTO `Usuarios`(`id_usuario`, `nombre`, `apellido`,`email`,`password`, `rol`) VALUES (?,?,?,?,?,?)";
       try{
            pStatement = con.prepareStatement(sql);
            pStatement.setNull(1, 0);
            pStatement.setString(2,usu.getNombre());
            pStatement.setString(3,usu.getApellido());
            pStatement.setString(4, usu.getEmail());
            pStatement.setString(5, usu.getPass());
            pStatement.setString(6, usu.getRol());
            pStatement.execute();
            result = true;
       }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
       }
       return result;
    }
   
   public ObservableList<Integer> obtenerCodigosInventarioProductos(){
        ObservableList<Integer> codigos = FXCollections.observableArrayList();
        sql = "select * from productos";
        try{
            query = con.createStatement();
            response = query.executeQuery(sql);
            while(response.next()){
                codigos.add(response.getInt("codigo"));
            }
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        return codigos;
    }
   
   public ObservableList<Productos> obtenerInventarioProductos(){
        ObservableList<Productos> productos = FXCollections.observableArrayList();
        sql = "select * from productos";
        try{
            query = con.createStatement();
            response = query.executeQuery(sql);
            while(response.next()){
                Productos p = new Productos();
                p.setCodigo(response.getInt("codigo"));
                p.setCantidad(response.getInt("cantidad"));
                p.setId(response.getInt("id_producto"));
                p.setNombre(response.getString("nombre"));
                p.setPrecio(response.getDouble("precio"));
                productos.add(p);
            }
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
        return productos;
    }
   
   public boolean agregarNuevoProducto(Productos p){
       boolean result = false;
       sql = "INSERT INTO `productos`(`id_producto`, `nombre`, `precio`, `cantidad`, `codigo`) VALUES (?,?,?,?,?)";
       try{
            pStatement = con.prepareStatement(sql);
            pStatement.setNull(1, 0);
            pStatement.setString(2,p.getNombre());
            pStatement.setDouble(3,p.getPrecio());
            pStatement.setInt(4, p.getCantidad());
            pStatement.setInt(5, p.getCodigo());
            pStatement.execute();
            result = true;
       }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
       }
       return result;
    }
   
   public Productos obtenerProducto(String codigo){
        Productos p = new Productos();
        sql = "select * from productos where codigo = "+codigo;
        try{
            query = con.createStatement();
            response = query.executeQuery(sql);
            while(response.next()){
                p.setId(response.getInt("id_producto"));
                p.setCantidad(response.getInt("cantidad"));
                p.setNombre(response.getString("nombre"));
                p.setPrecio(response.getDouble("precio"));
                p.setCodigo(response.getInt("codigo"));
             }
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
        return p;
    }
    public boolean actualizarProducto(String cantidad, String codigo){
       boolean result = false;
       sql = "UPDATE `productos` SET `cantidad`= "+cantidad+" WHERE `codigo`= "+codigo;
       try{
            pStatement = con.prepareStatement(sql);
            pStatement.execute();
            result = true;
       }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
       }
       return result;
    }
}
