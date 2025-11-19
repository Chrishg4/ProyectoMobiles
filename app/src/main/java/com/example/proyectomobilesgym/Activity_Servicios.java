package com.example.proyectomobilesgym;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_Servicios extends AppCompatActivity {

    private EditText txtBuscar;
    private ListView listaView;
    private CustomAdapterMembresia adaptador;
    private ListServicio lista;
    private int itemseleccionado;
    private AdminDB admin;
    private DAOServicio membresiaDB;

    private Button btnAgregar, btnEliminar, btnEditar, btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_servicios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAgregar = findViewById(R.id.btn_añadir);
        btnEliminar = findViewById(R.id.btn_eliminar);
        btnEditar = findViewById(R.id.btn_editar);
        btnSalir = findViewById(R.id.btn_salir);
        setItemSeleccionado(-1);
        btnAgregar.setBackgroundColor(R.color.verdenes);
        btnSalir.setBackgroundColor(R.color.amarillo);

        btnAgregar.setText(R.string.btn_add);
        btnEliminar.setText(R.string.btn_delete);
        btnEditar.setText(R.string.btn_edit);
        btnSalir.setText(R.string.btn_exit);

    }

    private void setItemSeleccionado(int position) {
        itemseleccionado = position;
        if (position == -1){
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            //cambiar color a gris
            btnEditar.setBackgroundColor(getResources().getColor(R.color.gris));
            btnEliminar.setBackgroundColor(getResources().getColor(R.color.gris));
        } else {
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
            //restaurare el color original;
            btnEditar.setBackgroundColor(getResources().getColor(R.color.verde));
            btnEliminar.setBackgroundColor(getResources().getColor(R.color.rojo));
        }
    }

}