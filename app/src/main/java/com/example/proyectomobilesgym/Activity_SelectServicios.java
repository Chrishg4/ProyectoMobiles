package com.example.proyectomobilesgym;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
    private EditText txtbuscar;

    public void salir(View view){
        finish();
    }

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

        txtbuscar = findViewById(R.id.editTextText);

        txtbuscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

    }

    public void buscar(){
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        DAOServicio servicioDB = new DAOServicio(db);
        String criterio = txtbuscar.getText().toString();
        lista = servicioDB.buscarPor(criterio);
        adaptador.setLista(lista);
    }

    private void inicializarLista() {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        DAOServicio servicioDB = new DAOServicio(db);
        lista = servicioDB.cargarTodos();
        adaptador.setLista(lista);
    }
}