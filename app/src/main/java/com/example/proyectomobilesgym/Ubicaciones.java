package com.example.proyectomobilesgym;

public class Ubicaciones {

    public double latitud;

    public double longitud;


    public Ubicaciones( double latitud, double longitud) {

        this.latitud = latitud;
        this.longitud = longitud;
    }


    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }


}
