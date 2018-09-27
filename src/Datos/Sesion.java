/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.util.Observable;

/**
 *
 * @author Edu
 */
public class Sesion extends Observable {
    
    private static Sesion instance = null;
    
    private Usuario currentUser = null;
    
    public static Sesion getInstance(){
        if(instance == null){
            instance = new Sesion();
        }
        return instance;
    }
    
    private Sesion(){}
    
    public Usuario getCurrentUser(){
        return currentUser;
    }
    
    public void setCurrentUser(Usuario user){
        currentUser = user;
        setChanged();
        notifyObservers("Se dio click");
    }
}
