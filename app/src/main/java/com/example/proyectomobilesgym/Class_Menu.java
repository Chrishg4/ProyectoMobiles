package com.example.proyectomobilesgym;

import java.util.List;

public class Class_Menu {
    public int id;
    public int imagen;
    public String txtPrincipal;


    public Class_Menu(int id, int imagen, String txtPrincipal) {
        this.id = id;
        this.imagen = imagen;
        this.txtPrincipal = txtPrincipal;

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

    public void setTxtPrincipal(String txtPrincipal) {
        this.txtPrincipal = txtPrincipal;
    }


}

