package com.example.proyectomobilesgym;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Activity_SelectServicios extends AppCompatActivity {

    private CustomAdapterServicio adaptador;
    private ListView listaView;
    private ListServicio lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_servicios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lista = new ListServicio();
        adaptador = new CustomAdapterServicio(this, lista);
        inicializarLista();
        listaView = findViewById(R.id.listServicios);
        listaView.setAdapter(adaptador);

        listaView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Seleccion.servicioSeleccionado = lista.get(position);
                    finish();
                }
        );
    }

    private void inicializarLista() {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        DAOServicio servicioDB = new DAOServicio(db);
        lista = servicioDB.cargarTodos();
        adaptador.notifyDataSetChanged();

    }
}