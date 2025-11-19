package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DAOMembresia {
    private SQLiteDatabase db;

    public DAOMembresia(SQLiteDatabase db) {
        this.db = db;
    }

    public long insertar(Membresia membresia) {

        ContentValues campos = new ContentValues();
        campos.put("tipo", membresia.getTipo().toString());
        campos.put("precioTotal", membresia.getPrecioTotal());
        campos.put("cedulaCliente", membresia.getCliente());
        campos.put("cedulaEntrenador", membresia.getEntrenador());

        long codigoMembresia = db.insert("membresias", null, campos);

        // insertar en la tabla intermedia
        for (Servicio servicio : membresia.getServicios()) {
            campos = new ContentValues();
            campos.put("codigoMembresia", codigoMembresia);
            campos.put("codigoServicio", servicio.getCodigo());
            db.insert("membresiaServicio", null, campos);
        }

//        double precio = getServiciosDeMembresia((int)codigoMembresia).getPrecioTotal();;
//        membresia.setPrecioTotal(precio);
//        inicializarPrecioTotal(codigoMembresia, precio);

        return codigoMembresia;
    }

//    private void inicializarPrecioTotal(long codigoMembresia, double precio) {
//        ContentValues campos = new ContentValues();
//        campos.put("precioTotal", precio);
//        db.update("membresias", campos, "codigo = ?", new String[]{String.valueOf(codigoMembresia)});
//    }


    public boolean actualizar(Membresia membresia) {

        ContentValues campos = new ContentValues();
        campos.put("tipo", membresia.getTipo().toString());
        campos.put("precioTotal", membresia.getPrecioTotal());
        campos.put("cedulaCliente", membresia.getCliente());
        campos.put("cedulaEntrenador", membresia.getEntrenador());

        int filas = db.update("membresias", campos, "codigo = ?", new String[]{String.valueOf(membresia.getCodigo())});

        // borrar servicios existentes
        db.delete("membresiaServicio", "codigoMembresia = ?", new String[]{String.valueOf(membresia.getCodigo())});

        // insertar servicios de nuevo
        for (Servicio servicio : membresia.getServicios()) {
            ContentValues rel = new ContentValues();
            rel.put("codigoMembresia", membresia.getCodigo());
            rel.put("codigoServicio", servicio.getCodigo());
            db.insert("membresiaServicio", null, rel);
        }

        return filas > 0;
    }

    public boolean eliminar(int codigo) {

        // primero borrar relaciones
        db.delete("membresiaServicio", "codigoMembresia = ?", new String[]{String.valueOf(codigo)});

        // luego borrar membresia
        int filas = db.delete("membresias", "codigo = ?", new String[]{String.valueOf(codigo)});

        return filas > 0;
    }

    public ListMembresia buscarPor(String criterio) {

        ListMembresia lista = new ListMembresia();

        criterio = "%" + criterio.trim() + "%";

        Cursor cursor = db.rawQuery("SELECT m.codigo, m.tipo, m.precioTotal, m.cedulaCliente, r.cedulaEntrenador FROM membresia m JOIN clientes c ON m.cedulaCliente = c.cedula JOIN rutina r ON m.codigoRutina = r.codigo JOIN entrenador e ON r.cedulaEntrenador = e.cedula WHERE m.codigo LIKE ? OR c.nombre LIKE ? OR e.nombre LIKE ?", new String[]{criterio, criterio, criterio});

        while (cursor.moveToNext()) {
            int codigo = cursor.getInt(0);
            TipoMembresia tipo = TipoMembresia.valueOf(cursor.getString(1));
            double precioTotal = cursor.getDouble(2);
            int cliente = Integer.parseInt(cursor.getString(3));
            int entrenador = Integer.parseInt(cursor.getString(4));
            ListServicio servicios = getServiciosDeMembresia(codigo);
            Membresia membresia = new Membresia(codigo, tipo, precioTotal, cliente, entrenador, servicios);

            lista.add(membresia);
        }

        cursor.close();
        return lista;
    }

    public ListMembresia cargarTodos() {

        ListMembresia lista = new ListMembresia();

        Cursor cursor = db.rawQuery("SELECT codigo, tipo, precioTotal, cedulaCliente, cedulaEntrenador FROM membresias", null);

        while (cursor.moveToNext()) {
            int codigo = cursor.getInt(0);
            TipoMembresia tipo = TipoMembresia.valueOf(cursor.getString(1));
            double precioTotal = cursor.getDouble(2);
            int cliente = Integer.parseInt(cursor.getString(3));
            int entrenador = Integer.parseInt(cursor.getString(4));
            ListServicio servicios = getServiciosDeMembresia(codigo);
            Membresia membresia = new Membresia(codigo, tipo, precioTotal, cliente, entrenador, servicios);

            lista.add(membresia);
        }

        cursor.close();
        return lista;
    }

    private ListServicio getServiciosDeMembresia(int codigo) {
        ListServicio servicios = new ListServicio();

        Cursor cursor = db.rawQuery("SELECT s.codigo, s.nombre, s.precio FROM servicios s INNER JOIN membresiaServicios ms ON s.codigo = ms.codigoServicio WHERE ms.codigoMembresia = ?", new String[]{String.valueOf(codigo)});

        while (cursor.moveToNext()) {
            Servicio servicio = new Servicio(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2));
            servicios.add(servicio);
        }

        cursor.close();
        return servicios;
    }
}
