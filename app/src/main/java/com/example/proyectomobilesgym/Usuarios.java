package com.example.proyectomobilesgym;

public class Usuarios {
    public int id;
    public int imagen;
    public String txtPrincipal;
    public String txtSecundario;

    public Usuarios(int id, int imagen, String txtPrincipal, String txtSecundario) {
        this.id = id;
        this.imagen = imagen;
        this.txtPrincipal = txtPrincipal;
        this.txtSecundario = txtSecundario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getTxtPrincipal() {
        return txtPrincipal;
    }

    public void settxtPrincipal(String txtPrincipal) {
        this.txtPrincipal = txtPrincipal;
    }

    public String getTxtSecundario() {
        return txtSecundario;
    }

    public void settxtSecundario(String txtSecundario) {
        this.txtSecundario = txtSecundario;
    }

}

