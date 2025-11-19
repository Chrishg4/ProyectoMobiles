package com.example.proyectomobilesgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterServicio extends BaseAdapter {
    Context context;
    ListServicio lst;

    public CustomAdapterServicio(Context context, ListServicio lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Servicio getItem(int i) {
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

        Servicio servicio=lst.get(i);

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.listview_servicios,null);

        //ImageViewContacto=view.findViewById(R.id.imageViewContacto);
        TextViewNombre=view.findViewById(R.id.textViewNombreServicio);
        TextViewDes=view.findViewById(R.id.textViewDesServicio);

        //ImageViewContacto.setImageResource(c.imagen);
        String nombre = servicio.getNombre();
        String detalles = "₡" + String.valueOf(servicio.getPrecio());
        TextViewNombre.setText(nombre);
        TextViewDes.setText(detalles);

        return view;

    }
    public void remove(Servicio s) {
        lst.remove(s.getCodigo());
        notifyDataSetChanged();
    }

    public void setLista(ListServicio lista) {
        this.lst = lista;
        notifyDataSetChanged();
    }
}
