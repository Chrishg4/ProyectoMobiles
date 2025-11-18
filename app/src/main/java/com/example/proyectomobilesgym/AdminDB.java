package com.example.proyectomobilesgym;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminDB extends SQLiteOpenHelper {

    public AdminDB(@Nullable Context context) {
        super(context, "gimnasio", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE clientes (cedula TEXT PRIMARY KEY, nombre TEXT NOT NULL, numero INTEGER, genero TEXT CHECK (genero IN ('HOMBRE','MUJER')), edad INTEGER, altura REAL, peso REAL)");

        db.execSQL("CREATE TABLE entrenadores (cedula TEXT PRIMARY KEY, nombre TEXT NOT NULL, contacto TEXT)");

        db.execSQL("CREATE TABLE servicios (codigo INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, precio REAL)");

        db.execSQL("CREATE TABLE membresias (codigo INTEGER PRIMARY KEY AUTOINCREMENT, tipo TEXT CHECK (tipo IN ('mensual','trimestral','anual','especial')), precioTotal REAL, cedulaCliente TEXT, cedulaEntrenador TEXT, FOREIGN KEY(cedulaCliente) REFERENCES clientes(cedula), FOREIGN KEY(cedulaEntrenador) REFERENCES entrenadores(cedula))");

        db.execSQL("CREATE TABLE membresiaServicios (codigo INTEGER PRIMARY KEY AUTOINCREMENT, codigoMembresia INTEGER, codigoServicio INTEGER, FOREIGN KEY(codigoMembresia) REFERENCES membresias(codigo), FOREIGN KEY(codigoServicio) REFERENCES servicios(codigo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
