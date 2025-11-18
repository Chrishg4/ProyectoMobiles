package com.example.proyectomobilesgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ClientesAdapter extends BaseAdapter {
    Context context;

    List<Clientes> lstClientes;

    public ClientesAdapter(Context context, List<Clientes> lstClientes) {
        this.context = context;
        this.lstClientes = lstClientes;
    }

    @Override
    public int getCount() {
        return lstClientes.size();// devuelve el tamaño de la lista
    }

    @Override
    public Clientes getItem(int i) {
        return lstClientes.get(i);// devuelve el objeto en la posición i
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageViewGenero;
        TextView textViewNombre;
        TextView textViewCedula;

        Clientes cliente = lstClientes.get(i);

        if (view == null) {// si la vista es nula, infla el diseño
            view = LayoutInflater.from(context).inflate(R.layout.listview_clientes, null);  // infla el diseño personalizado para cada elemento de la lista
        }

        Genero genero = cliente.getGenero();



        imageViewGenero = view.findViewById(R.id.imageViewGenero);
        textViewNombre = view.findViewById(R.id.textViewNombre);
        textViewCedula = view.findViewById(R.id.textViewCedula);

        // Asignar la imagen según el género
        if (genero == Genero.HOMBRE) {
            imageViewGenero.setImageResource(R.drawable.user_h);
        } else if (genero == Genero.MUJER) {
            imageViewGenero.setImageResource(R.drawable.user_m);
        } else {
            imageViewGenero.setImageResource(R.drawable.ic_launcher_foreground);
        }

        textViewNombre.setText(cliente.nombre);
        textViewCedula.setText(cliente.cedula);

        return view;
    }

    public void remove(Clientes c) {// elimina un cliente de la lista y notifica el cambio
        lstClientes.remove(c);
        notifyDataSetChanged();
    }
}
