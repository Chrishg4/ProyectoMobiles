package com.example.proyectomobilesgym;

import java.util.HashMap;
public class ListMembresia {
    private HashMap<Integer,Membresia> lista;

    public  ListMembresia(){
        lista = new HashMap<>();
    }
    public ListMembresia(HashMap<Integer,Membresia> lista){
        this.lista = lista;
    }
    public ListMembresia(Membresia[] membresias){
        this.lista = new HashMap<>();
        for (Membresia m: membresias){
            lista.put(m.getCodigo(),m);
        }
    }
    public Membresia get(int codigo){
        return lista.get(codigo);
    }
    public void put(Membresia m){
        lista.put(m.getCodigo(),m);
    }
    public void remove(int indice){
        lista.remove(indice);
    }
    public int size(){
        return lista.size();
    }
}
