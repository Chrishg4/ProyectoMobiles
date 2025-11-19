package com.example.proyectomobilesgym;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
        try {
            Intent intent = new Intent(this, Activity_AdmMembresias.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Error al agregar la membresia", Toast.LENGTH_SHORT).show();
        }
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

        btnAgregar.setText(R.string.btn_add);
        btnEliminar.setText(R.string.btn_delete);
        btnEditar.setText(R.string.btn_edit);
        btnSalir.setText(R.string.btn_exit);

        admin = new AdminDB(this);
        membresiaDB = new DAOMembresia(admin.getReadableDatabase());

        txtBuscar = findViewById(R.id.textBuscar);
        listaView = findViewById(R.id.listMembresias);

        lista = membresiaDB.cargarTodos();

        adaptador = new CustomAdapterMembresia(this, lista);
        listaView.setAdapter(adaptador);

        listaView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    for (int i = 0; i < listaView.getChildCount(); i++)
                        listaView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(Color.LTGRAY);
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
            //cambiar color a gris
            deshabilitarBotones();
        } else {
            //restaurare el color original;
            btnEditar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));
            btnEliminar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D33232")));
        }
    }

    public void deshabilitarBotones() {
        //btnEliminarCliente.setEnabled(false);
        btnEliminar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        //btnEditarCliente.setEnabled(false);
        btnEditar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
    }

}