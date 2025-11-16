package com.example.proyectomobilesgym;

import java.util.HashMap;

public class ListServicio {
    private HashMap<Integer,Servicio> lista;

    public  ListServicio(){
        lista = new HashMap<>();
    }
    public ListServicio(HashMap<Integer,Servicio> lista){
        this.lista = lista;
    }
    public ListServicio(Servicio[] servicios){
        this.lista = new HashMap<>();
        for (Servicio s: servicios){
            lista.put(s.getCodigo(),s);
        }
    }
    public Servicio[] getLista(){
        Servicio[] servicios = new Servicio[lista.size()];
        int i = 0;
        for (Servicio s: lista.values()){
            servicios[i] = s;
            i++;
        }
        return servicios;
    }
    public Servicio get(int indice){
        return lista.get(indice);
    }
    public void put(Servicio s){
        lista.put(s.getCodigo(),s);
    }
    public void remove(int indice){
        lista.remove(indice);
    }
    public int size(){
        return lista.size();
    }
}
