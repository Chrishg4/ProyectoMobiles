package com.example.proyectomobilesgym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UbicacionesAdapter extends BaseAdapter {
    Context context;

    List<Ubicaciones> lstUbicaciones;

    public UbicacionesAdapter(Context context, List<Ubicaciones> lstUbicaciones) {
        this.context = context;
        this.lstUbicaciones = lstUbicaciones;
    }


    @Override
    public int getCount() {
        return lstUbicaciones.size();// devuelve el tamaño de la lista
    }

    @Override
    public Ubicaciones getItem(int i) {
        return lstUbicaciones.get(i);// devuelve el objeto en la posición i
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageUbicacion;
        TextView textViewNombre;
        TextView textViewLatitud;
        TextView textViewLongitud;

        Ubicaciones ubicacion = lstUbicaciones.get(i); // obtiene el objeto en la posición i

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_ubicaciones, null);// infla la vista
        }

        imageUbicacion = view.findViewById(R.id.imgUbicacion);
        textViewNombre = view.findViewById(R.id.txtNombreUbicacion);
        textViewLatitud = view.findViewById(R.id.txtLatitudUbicacion);
        textViewLongitud = view.findViewById(R.id.txtLongitudUbicacion);

        imageUbicacion.setImageResource(R.drawable.locationmain);
        textViewNombre.setText(ubicacion.nombre);
        //los double
        textViewLatitud.setText(String.valueOf(ubicacion.latitud));
        textViewLongitud.setText(String.valueOf(ubicacion.longitud));



        return view;
    }

    public void remove(Ubicaciones ubicacion) {
        lstUbicaciones.remove(ubicacion);
        notifyDataSetChanged();
    }


}
