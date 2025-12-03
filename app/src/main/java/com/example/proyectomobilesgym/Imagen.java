package com.example.proyectomobilesgym;

public class Imagen {
    private byte[] imagenEnBytes;

    public Imagen(byte[] imagenEnBytes) {
        this.imagenEnBytes = imagenEnBytes;
    }

    public byte[] getImagenEnBytes() {
        return imagenEnBytes;
    }

    public void setImagenEnBytes(byte[] imagenEnBytes) {
        this.imagenEnBytes = imagenEnBytes;
    }
}
