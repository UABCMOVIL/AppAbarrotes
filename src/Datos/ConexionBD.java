/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;
import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
/**
 *
 * @author Edu
 */
public class ConexionBD {
    private Connection con;
    public ConexionBD(){
        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://127.0.0.1:3306/tienda_abarrotes");
        ds.setUser("root");
        try {
            con = ds.getConnection();
            System.out.println("Se conecto ALV :)");
        } catch (Exception ex) {
            System.out.println("NO SE CONECTO u.u");
        }
    }
    
    public void closeConexion(){
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println("No habia conexiones abiertas! :" + ex.getMessage());
        }
    }
    
    public Connection getConexion(){
        return con;
    }
}
