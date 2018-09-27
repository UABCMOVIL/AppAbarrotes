/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author Edu
 */
public class DataHolder extends Observable {
    private static DataHolder instance = null;
    private List<String> data = new LinkedList<>();
    
    public static DataHolder getInstance(){
        if(instance == null){
            instance = new DataHolder();
        }
        return instance;
    }
    
    private DataHolder(){}
    
    public void setData(String[] data,String action){
        this.data.clear();
        for(int i=0;i < data.length;i++){
            this.data.add(data[i]);
        }
        setChanged();
        notifyObservers(action);
    }
    
    public List<String> getData(){
        return data;
    }
}
