package com.example.proyectomobilesgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterMenu extends BaseAdapter {
    Context context;
    List<Class_Menu> lst;

    public CustomAdapterMenu(Context context, List<Class_Menu> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView ImageViewMenu;
        TextView TextViewPrincipal;


        Class_Menu c=lst.get(i);

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.listview_menu,null);

        ImageViewMenu=view.findViewById(R.id.imageViewMenu);
        TextViewPrincipal=view.findViewById(R.id.textViewPrincipal);


        ImageViewMenu.setImageResource(c.imagen);
        TextViewPrincipal.setText(c.txtPrincipal);

        return view;

    }
}

