package com.example.proyectomobilesgym;

import java.util.List;

public class Membresia {

    private int codigo;
    private TipoMembresia tipo;
    private double precioTotal;
    private String cliente;
    private String entrenador;
    private ListServicio servicios;

    public Membresia(int codigo, TipoMembresia tipo, double precioTotal, String cliente, String entrenador, ListServicio servicios) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.precioTotal = precioTotal;
        this.cliente = cliente;
        this.entrenador = entrenador;
        this.servicios = servicios;
    }

    public int getCodigo() {
        return codigo;
    }

    protected void setCodigo(int codigo) {
        this.codigo = codigo;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(String entrenador) {
        this.entrenador = entrenador;
    }
    public List<Servicio> getServicios() {
        return servicios.getLista();
    }
}
