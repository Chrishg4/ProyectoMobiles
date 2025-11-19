package com.example.proyectomobilesgym;

public class Servicio {
    private int codigo;
    private String nombre;
    private double precio;

    public Servicio(int codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getCodigo() {
        return codigo;
    }
    public boolean setCodigo(int codigo) {
        if (codigo<=0 || this.codigo != -1){
            return false;
        }
        this.codigo = codigo;
        return true;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean setNombre(String nombre) {
        if (nombre==null || nombre.isEmpty()){
            return false;
        }
        this.nombre = nombre;
        return true;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean setPrecio(double precio) {
        if (precio<=0){
            return false;
        }
        this.precio = precio;
        return true;
    }
}
