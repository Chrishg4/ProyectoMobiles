package com.example.proyectomobilesgym;

import java.util.List;

public class Membresia {

    private int codigo;
    private TipoMembresia tipo;
    private double precioTotal;
    private int cliente;
    private int entrenador;
    private ListServicio servicios;

    public Membresia(int codigo, TipoMembresia tipo, double precioTotal, int cliente, int entrenador, ListServicio servicios) {
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

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public int getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(int entrenador) {
        this.entrenador = entrenador;
    }
    public List<Servicio> getServicios() {
        return servicios.getLista();
    }
}
