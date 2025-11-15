package com.example.proyectomobilesgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Usuarios> lst;

    public CustomAdapter(Context context, List<Usuarios> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Usuarios getItem(int i) {
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

        Usuarios c=lst.get(i);

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.listview_usuarios,null);

        ImageViewContacto=view.findViewById(R.id.imageViewContacto);
        TextViewNombre=view.findViewById(R.id.textViewNombre);
        TextViewDes=view.findViewById(R.id.textViewDes);

        ImageViewContacto.setImageResource(c.imagen);
        TextViewNombre.setText(c.txtPrincipal);
        TextViewDes.setText(c.txtSecundario);

        return view;

    }
}

