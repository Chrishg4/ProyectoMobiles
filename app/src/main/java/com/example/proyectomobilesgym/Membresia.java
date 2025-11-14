package com.example.proyectomobilesgym;

public class Membresia {

    private int id;
    private TipoMembresia tipo;
    private double precioTotal;
    private Object cliente;
    private Object entrenador;

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public TipoMembresia getTipo() {
        return tipo;
    }

    public void setTipo(TipoMembresia tipo) {
        this.tipo = tipo;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Object getCliente() {
        return cliente;
    }

    public void setCliente(Object cliente) {
        this.cliente = cliente;
    }

    public Object getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Object entrenador) {
        this.entrenador = entrenador;
    }
}
