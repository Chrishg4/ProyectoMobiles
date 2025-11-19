package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DAOServicio {
    private SQLiteDatabase db;
    public DAOServicio(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertar(Servicio servicio){
        ContentValues campos = new ContentValues();
        campos.put("nombre", servicio.getNombre());
        campos.put("precio", servicio.getPrecio());

        return db.insert("servicios", null, campos);
    }

    public boolean eliminar(int codigo){
        int filas = db.delete("servicios", "codigo = ?", new String[]{String.valueOf(codigo)});
        return filas > 0;
    }

    public boolean actualizar(Servicio servicio){
        ContentValues campos = new ContentValues();
        campos.put("nombre", servicio.getNombre());
        campos.put("precio", servicio.getPrecio());

        int filas = db.update("servicios", campos, "codigo = ?", new String[]{String.valueOf(servicio.getCodigo())});
        return filas > 0;
    }

    public ListServicio buscarPor(String criterio){
        Cursor cursor = db.rawQuery("SELECT codigo, nombre, precio FROM servicios WHERE nombre LIKE ?", new String[]{"%" + criterio + "%"});
        ListServicio lista = new ListServicio();
        while (cursor.moveToNext()){
            int codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
            Servicio servicio = new Servicio(codigo, nombre, precio);
            lista.add(servicio);
        }
        cursor.close();
        return lista;
    }


    public ListServicio cargarTodos(){
        Cursor cursor = db.rawQuery("SELECT codigo, nombre, precio FROM servicios", null);
        ListServicio lista = new ListServicio();
        while (cursor.moveToNext()){
            int codigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
            Servicio servicio = new Servicio(codigo, nombre, precio);
            lista.add(servicio);
        }
        cursor.close();
        return lista;
    }

    public Servicio buscarPorCodigo(int codigo) {
        Cursor cursor = db.rawQuery("SELECT codigo, nombre, precio FROM servicios WHERE codigo = ?", new String[]{String.valueOf(codigo)});
        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
            cursor.close();
            return new Servicio(codigo, nombre, precio);
        } else {
            cursor.close();
            return null;
        }
    }

    public ListServicio buscarPorMembresia(int codigo) {
        Cursor cursor = db.rawQuery("SELECT s.codigo, s.nombre, s.precio FROM servicios s JOIN servicios_membresias sm ON s.codigo = sm.servicio_codigo WHERE sm.membresia_codigo = ?", new String[]{String.valueOf(codigo)});
        ListServicio lista = new ListServicio();
        if (cursor.moveToFirst()) {
            do {
                int servicioCodigo = cursor.getInt(cursor.getColumnIndexOrThrow("codigo"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio"));
                Servicio servicio = new Servicio(servicioCodigo, nombre, precio);
                lista.add(servicio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }
}
