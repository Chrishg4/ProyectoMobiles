package com.example.proyectomobilesgym;

public class Clientes {
    public String cedula;
    public int imagen;
    public String nombre;
    public String numero;
    public Genero genero;
    public int edad;
    public double altura;
    public double peso;

    public Clientes(String cedula, String nombre, String numero, Genero genero, int edad, double altura, double peso) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.imagen = imagen;
        this.numero = numero;
        this.genero = genero;
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNumero() {
        return numero;
    }

    public Genero getGenero() {
        return genero;
    }

    public int getEdad() {
        return edad;
    }

    public double getAltura() {
        return altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
