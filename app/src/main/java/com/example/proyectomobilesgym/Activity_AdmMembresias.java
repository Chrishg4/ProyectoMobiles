package com.example.proyectomobilesgym;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_AdmMembresias extends AppCompatActivity {

    private EditText edtCedula, edtNombreCliente;
    private EditText edtCedulaEntrenador, edtNombreEntrenador;
    private Spinner spTipo;
    private EditText edtCodigoServicio, edtNombreServicio, edtPrecioServicio, edtPrecioTotal;
    private Button btnAgregarServicio, btnEditarServicio, btnEliminarServicio;
    private ListView listServicios;
    private Button btnGuardar, btnReiniciar, btnCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_membresias);

        edtCedula = findViewById(R.id.edtCedula);
        edtNombreCliente = findViewById(R.id.edtNombreCliente);

        // Entrenador
        edtCedulaEntrenador = findViewById(R.id.edtCedulaEntrenador);
        edtNombreEntrenador = findViewById(R.id.edtNombreEntrenador);

        // Tipo (spinner)
        spTipo = findViewById(R.id.spTipo);

        // Servicios
        edtCodigoServicio = findViewById(R.id.edtCodigoServicio);
        edtNombreServicio = findViewById(R.id.edtNombreServicio);
        edtPrecioServicio = findViewById(R.id.edtPrecioServicio);
        edtPrecioTotal = findViewById(R.id.edtPrecioTotal);

        btnAgregarServicio = findViewById(R.id.btnAgregarServicio);
        btnEditarServicio = findViewById(R.id.btnEditarServicio);
        btnEliminarServicio = findViewById(R.id.btnEliminarServicio);

        listServicios = findViewById(R.id.listServicios);

        // Botones generales de la pantalla
        btnGuardar = findViewById(R.id.btnGuardar);
        btnReiniciar = findViewById(R.id.btnSalir);
        btnCancelar = findViewById(R.id.btnCancelar);

        ArrayAdapter<TipoMembresia> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TipoMembresia.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);
    }


}