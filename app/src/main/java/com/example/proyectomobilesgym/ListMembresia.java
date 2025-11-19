package com.example.proyectomobilesgym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListMembresia {
    private List<Membresia> lista;

    public  ListMembresia(){
        lista = new ArrayList<>();
    }
    public ListMembresia(List<Membresia> lista){
        this.lista = lista;
    }
    public Membresia get(int codigo){
        return lista.get(codigo);
    }
    public void add(Membresia m){
        lista.add(m);
    }
    public void remove(int indice){
        lista.remove(indice);
    }
    public int size(){
        return lista.size();
    }
}
