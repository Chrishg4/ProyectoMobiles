package com.example.proyectomobilesgym;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Activity_Membresias extends AppCompatActivity {

    private EditText txtBuscar;
    private ListView listaView;
    private CustomAdapterMembresia adaptador;
    private ListMembresia lista;
    private int itemseleccionado;
    private AdminDB admin;
    private DAOMembresia membresiaDB;

    private Button btnAgregar, btnEliminar, btnEditar, btnSalir;

    public void agregar(View view){
        Intent intent = new Intent(this, Activity_AdmMembresias.class);
        startActivity(intent);
    }

    public void editar(View view){
        if(itemseleccionado != -1){
            Membresia membresia = lista.get(itemseleccionado);
            Intent intent = new Intent(this, Activity_AdmMembresias.class);
            intent.putExtra("codigo", membresia.getCodigo());
            intent.putExtra("cliente", membresia.getCliente());
            intent.putExtra("entrenador", membresia.getEntrenador());
            intent.putExtra("tipo", membresia.getTipo());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Seleccione una membresia primero", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminar(View view){
        if(itemseleccionado != -1){
            Membresia membresia = lista.get(itemseleccionado);
            boolean eliminado = membresiaDB.eliminar(membresia.getCodigo());
            if(eliminado){
                Toast.makeText(this, "Membresia eliminada", Toast.LENGTH_SHORT).show();
                adaptador.remove(membresia);
                setItemSeleccionado(-1);
            } else {
                Toast.makeText(this, "Error al eliminar la membresia", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Seleccione una membresia primero", Toast.LENGTH_SHORT).show();
        }
    }

    public void salir(View view){
        finish();
    }

    public void buscar(){
        String criterio = txtBuscar.getText().toString();
        lista = membresiaDB.buscarPor(criterio);
        adaptador.setLista(lista);
        setItemSeleccionado(-1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_membresias);
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


        admin = new AdminDB(this);
        membresiaDB = new DAOMembresia(admin.getReadableDatabase());

        txtBuscar = findViewById(R.id.textBuscar);
        listaView = findViewById(R.id.listMembresias);

        lista = membresiaDB.cargarTodos();

        adaptador = new CustomAdapterMembresia(this, lista);
        listaView.setAdapter(adaptador);

        listaView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    setItemSeleccionado(position);
                }
        );

        txtBuscar.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscar();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

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