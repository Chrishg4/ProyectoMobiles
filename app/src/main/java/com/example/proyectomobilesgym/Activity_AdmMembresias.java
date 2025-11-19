package com.example.proyectomobilesgym;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_AdmMembresias extends AppCompatActivity {
    private boolean editar = false;
    private Servicio enEdicionServicio = null;
    private EditText edtCedula, edtNombreCliente;
    private EditText edtCedulaEntrenador, edtNombreEntrenador;
    private Spinner spTipo;
    private EditText edtCodigoServicio, edtNombreServicio, edtPrecioServicio, edtPrecioTotal;
    private Button btnAgregarServicio, btnEditarServicio, btnEliminarServicio;
    private ListView listaView;
    private int itemseleccionado;
    private Button btnGuardar, btnReiniciar, btnCancelar;
    private CustomAdapterServicio adaptador;
    private AdminDB admin;
    private DAOMembresia membresiaDB;
    private DAOServicio servicioDB;
    private ListServicio lista;


    public void agregarServicio(View view) {
        if (edtCodigoServicio.getText().toString().isEmpty() ||
                edtNombreServicio.getText().toString().isEmpty() ||
                edtPrecioServicio.getText().toString().isEmpty()) {
            return;
        }
        Servicio servicio = new Servicio(
                Integer.parseInt(edtCodigoServicio.getText().toString()),
                edtNombreServicio.getText().toString(),
                Double.parseDouble(edtPrecioServicio.getText().toString())
        );
        if (editar){
            servicioDB.actualizar(servicio);
            editar = false;
        } else {
            servicioDB.insertar(servicio);
        }
        lista.add(servicio);
        adaptador.notifyDataSetChanged();
        edtCodigoServicio.setText("");
        edtNombreServicio.setText("");
        edtPrecioServicio.setText("");
        setItemSeleccionado(-1);
        enEdicionServicio = null;
    }

    public void editarServicio(View view) {
        if (editar) {
            editar = false;
            edtCodigoServicio.setText("");
            edtNombreServicio.setText("");
            edtPrecioServicio.setText("");
            setItemSeleccionado(-1);

            if (enEdicionServicio == null) return;
            lista.add(enEdicionServicio);
            adaptador.notifyDataSetChanged();
            enEdicionServicio = null;
            return;
        }
        if (itemseleccionado == -1){
            Toast.makeText(this, getString(R.string.toast_no_service_selected), Toast.LENGTH_SHORT).show();
            return;
        }
        Servicio servicio = lista.get(itemseleccionado);
        lista.remove(itemseleccionado);
        edtCodigoServicio.setText(String.valueOf(servicio.getCodigo()));
        edtNombreServicio.setText(servicio.getNombre());
        edtPrecioServicio.setText(String.valueOf(servicio.getPrecio()));
        setItemSeleccionado(itemseleccionado);
        editar = true;
        enEdicionServicio = servicio;
    }

    public void eliminarServicio(View view) {
        if (itemseleccionado == -1){
            Toast.makeText(this, getString(R.string.toast_no_service_selected), Toast.LENGTH_SHORT).show();
            return;
        }
        Servicio servicio = lista.get(itemseleccionado);
        servicioDB.eliminar(servicio.getCodigo());
        adaptador.remove(servicio);
        setItemSeleccionado(-1);
    }

    public void irASeleccionarServicio(View view) {
        Seleccion.servicioSeleccionado = null;
        Intent intent = new Intent(this, Activity_SelectServicios.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Seleccion.servicioSeleccionado != null){
            lista.add(Seleccion.servicioSeleccionado);
            adaptador.notifyDataSetChanged();
            Seleccion.servicioSeleccionado = null;
        }
    }


    public void salir(View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_membresias);

        edtCedula = findViewById(R.id.edtCedula);
        edtNombreCliente = findViewById(R.id.edtNombreCliente);
        edtNombreCliente.setEnabled(false);

        edtCedulaEntrenador = findViewById(R.id.edtCedulaEntrenador);
        edtNombreEntrenador = findViewById(R.id.edtNombreEntrenador);
        edtNombreEntrenador.setEnabled(false);

        spTipo = findViewById(R.id.spTipo);

        edtCodigoServicio = findViewById(R.id.edtCodigoServicio);
        edtNombreServicio = findViewById(R.id.edtNombreServicio);
        edtPrecioServicio = findViewById(R.id.edtPrecioServicio);
        edtPrecioTotal = findViewById(R.id.edtPrecioTotal);
        edtPrecioTotal.setEnabled(false);


        btnAgregarServicio = findViewById(R.id.btnAgregarServicio);
        btnEditarServicio = findViewById(R.id.btnEditarServicio);
        btnEliminarServicio = findViewById(R.id.btnEliminarServicio);
        setItemSeleccionado(-1);

        listaView = findViewById(R.id.listServicios);

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

        admin = new AdminDB(this);
        membresiaDB = new DAOMembresia(admin.getWritableDatabase());
        servicioDB = new DAOServicio(admin.getWritableDatabase());

        lista = new ListServicio();
        adaptador = new CustomAdapterServicio(this, lista);
        listaView.setAdapter(adaptador);

    }

    private void setItemSeleccionado(int position) {
        itemseleccionado = position;
        if (position == -1){
            deshabilitarBotones();
            if (listaView == null || listaView.getChildCount() == 0)
                return;
            for (int i = 0; i < listaView.getChildCount(); i++)
                listaView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        } else {
            btnEditarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));
            btnEliminarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D33232")));
        }
    }

    public void deshabilitarBotones() {
        btnEliminarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        btnEditarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
    }

}