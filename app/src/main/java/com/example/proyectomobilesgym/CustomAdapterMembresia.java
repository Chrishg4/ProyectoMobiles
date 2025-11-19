package com.example.proyectomobilesgym;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterMembresia extends BaseAdapter {
    Context context;
    ListMembresia lst;

    public CustomAdapterMembresia(Context context, ListMembresia lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Membresia getItem(int i) {
        return lst.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView ImageViewContacto;
        TextView TextViewNombre;
        TextView TextViewDes;

        Membresia membresia=lst.get(i);

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.listview_membresias,null);

        //ImageViewContacto=view.findViewById(R.id.imageViewContacto);
        TextViewNombre=view.findViewById(R.id.textViewNombre);
        TextViewDes=view.findViewById(R.id.textViewDetalles);

        //ImageViewContacto.setImageResource(c.imagen);
        String nombre = context.getString(R.string.header_membership) + " " + membresia.getCodigo() + " - " + membresia.getTipo().toString().toLowerCase();
        String detalles = cargarNombre(membresia.getCliente(), "clientes") + " - " + cargarNombre(membresia.getEntrenador(), "entrenadores") +  " : " + String.valueOf(membresia.getPrecioTotal());
        TextViewNombre.setText(nombre);
        TextViewDes.setText(detalles);

        return view;

    }

    private String cargarNombre(String cedula, String tabla){
        String valor = "";
        AdminDB admin = new AdminDB(context);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM " + tabla + " WHERE cedula = '" + cedula + "'", null);
        if (cursor.moveToFirst()) {
            valor = cursor.getString(0);
        }
        cursor.close();
        return valor;
    }

    public void remove(Membresia m) {
        lst.remove(m.getCodigo());
        notifyDataSetChanged();
    }

    public void setLista(ListMembresia lista) {
        this.lst = lista;
        notifyDataSetChanged();
    }
}
