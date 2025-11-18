package com.example.proyectomobilesgym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListServicio {
    private List<Servicio> lista;

    public  ListServicio(){
        lista = new ArrayList<>();
    }
    public List<Servicio> getLista(){
        return List.copyOf(lista);
    }
    public Servicio get(int indice){
        return lista.get(indice);
    }
    public void add(Servicio s){
        lista.add(s);
    }
    public void remove(int indice){
        lista.remove(indice);
    }
    public int size(){
        return lista.size();
    }
}
