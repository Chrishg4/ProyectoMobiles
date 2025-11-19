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

}
