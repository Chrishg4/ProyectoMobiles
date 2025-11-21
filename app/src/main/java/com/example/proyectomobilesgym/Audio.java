package com.example.proyectomobilesgym;

public class Audio {
    private String ruta;
    private byte[] enBytes;

    public Audio(String ruta) {
        this.ruta = ruta;
        this.enBytes = null;
    }
    public Audio(){
        this.ruta = "audio_temp";
        this.enBytes = null;
    }

    public void setEnBytes(byte[] enBytes) {
        this.enBytes = enBytes;
    }

    public byte[] getEnBytes() {
        return enBytes;
    }

    public String getRuta() {
        return ruta;
    }

}
