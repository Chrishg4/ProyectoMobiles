package com.example.proyectomobilesgym;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Activity_SelectEntrenadores extends AppCompatActivity {

    private CustomAdapter adaptador;
    private ListView listaView;
    private List<Usuarios> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_entrenadores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lista = new ArrayList<>();
        adaptador = new CustomAdapter(this, lista);
        inicializarLista();
        listaView = findViewById(R.id.listEntrenadores);
        listaView.setAdapter(adaptador);

        listaView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    Seleccion.entrenadorSeleccionado = lista.get(position);
                    finish();
                }
        );
    }

    private void inicializarLista() {
            AdminDB admin = new AdminDB(this);
            SQLiteDatabase db = admin.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT cedula, nombre, contacto FROM entrenadores",
                    null
            );

            lista.clear();

            if (cursor.moveToFirst()) {
                do {

                    String id = cursor.getString(0);
                    String nombre = cursor.getString(1);
                    String contacto = cursor.getString(2);

                    Usuarios u = new Usuarios(
                            id,
                            R.drawable.ic_launcher_foreground,
                            nombre,
                            contacto
                    );

                    lista.add(u);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

            adaptador.notifyDataSetChanged();

    }
}