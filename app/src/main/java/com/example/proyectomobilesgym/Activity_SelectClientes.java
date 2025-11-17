package com.example.proyectomobilesgym;

import android.database.Cursor;
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

public class Activity_SelectClientes extends AppCompatActivity {

    ClientesAdapter adapterClientes;
    List<Clientes> datosClientes;
    ListView listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_clientes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datosClientes = new ArrayList<>();
        adapterClientes = new ClientesAdapter(this, datosClientes);
        cargarClientes();
        listaClientes.setAdapter(adapterClientes);

        listaClientes.setOnItemClickListener((parent, view, position, id) -> {
            Seleccion.clienteSeleccionado = datosClientes.get(position);
            finish();
        });
    }

    public void cargarClientes() {
        AdminDB adminDB = new AdminDB(this);
        SQLiteDatabase db = adminDB.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT cedula, nombre, numero, genero, edad, altura, peso FROM clientes", null
        );

        datosClientes.clear();

        if (cursor.moveToFirst()) {
            do {
                String cedula = cursor.getString(0);
                String nombre = cursor.getString(1);
                String numero = cursor.getString(2);
                String generoTexto = cursor.getString(3); // "HOMBRE" o "MUJER"
                int edad = cursor.getInt(4);
                double altura = cursor.getDouble(5);
                double peso = cursor.getDouble(6);

                Genero genero = Genero.valueOf(generoTexto); // Convierte texto a enum

                Clientes cliente = new Clientes(
                        cedula,
                        nombre,
                        numero,
                        genero,
                        edad,
                        altura,
                        peso
                );

                datosClientes.add(cliente);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        adapterClientes.notifyDataSetChanged();
    }


}