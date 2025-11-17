package com.example.proyectomobilesgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterMembresia extends BaseAdapter {
    Context context;
    List<Membresia> lst;

    public CustomAdapterMembresia(Context context, List<Membresia> lst) {
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
        String detalles = String.valueOf(membresia.getCliente()) + " - " + String.valueOf(membresia.getEntrenador()) + " : " + String.valueOf(membresia.getPrecioTotal());
        TextViewNombre.setText(nombre);
        TextViewDes.setText(detalles);

        return view;

    }
    public void remove(Usuarios u) {
        lst.remove(u);
        notifyDataSetChanged();
    }
}
