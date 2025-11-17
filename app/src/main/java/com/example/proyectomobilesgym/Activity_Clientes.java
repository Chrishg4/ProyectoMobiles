package com.example.proyectomobilesgym;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
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

import java.util.ArrayList;
import java.util.List;

public class Activity_Clientes extends AppCompatActivity {

    EditText BuscarClientes;

    ListView listaClientes;

    ClientesAdapter adapterClientes;

    List<Clientes> datosClientes;

    int itemseleccionado = -1;

    Button btnAgregarCliente, btnEliminarCliente, btnEditarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        BuscarClientes = findViewById(R.id.Buscar);
        listaClientes = findViewById(R.id.listClientes);
        btnAgregarCliente = findViewById(R.id.btnAgregar);
        btnEliminarCliente = findViewById(R.id.btnEditar);
        btnEditarCliente = findViewById(R.id.btnEliminar);

        datosClientes = new ArrayList<>();
        adapterClientes = new ClientesAdapter(this, datosClientes);
        listaClientes.setAdapter(adapterClientes);

        // al seleccionar un cliente en el listView resaltar el item seleccionado
        listaClientes.setOnItemClickListener((parent, view, position, id) -> {
            itemseleccionado = position;

            for (int i = 0; i < listaClientes.getChildCount(); i++)
                listaClientes.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

            view.setBackgroundColor(Color.LTGRAY);

            // habilitar botones de editar y eliminar al seleccionar un cliente
            btnEliminarCliente.setEnabled(true);
            btnEditarCliente.setEnabled(true);
        });

        // buscar cliente al escribir en el editText
        BuscarClientes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            // buscar cliente al cambiar el texto ya que esto actualiza automaticamente el listView
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarCliente();
            }
        });

        //inabilitar botones de editar y eliminar hasta que se seleccione un cliente
        btnEliminarCliente.setEnabled(false);
        btnEditarCliente.setEnabled(false);



    }

    public void volverAtras(View view) {
        finish();
    }

    //funcion para buscar por nombre en el editText BuscarCliente y actualizar el listView automaticamente
    public void buscarCliente() {

        String nombreBuscado = BuscarClientes.getText().toString().trim();// obtener el texto del EditText y eliminar espacios en blanco al inicio y al final
        if (nombreBuscado.isEmpty()) {
            cargarClientes();// si el texto esta vacio cargar todos los clientes
            return;
        }
        AdminDB adminDB = new AdminDB(this);
        SQLiteDatabase db = adminDB.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT cedula, nombre, numero, genero, edad, altura, peso FROM clientes WHERE nombre LIKE ? OR cedula LIKE ?",
                new String[]{"%" + nombreBuscado + "%" , "%" + nombreBuscado + "%"}
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

    public void agregarCliente(View view) {
        Intent intent = new Intent(this, Activity_AdmClientes.class);
        startActivity(intent);
    }

    // elimina el cliente seleccionado del listView de la base de datos y de la lista
    public void eliminarCliente(View view) {
        if (itemseleccionado >= 0) {
            Clientes clienteSelec = adapterClientes.getItem(itemseleccionado);

            eliminarporNombre(clienteSelec.getNombre());

            adapterClientes.remove(clienteSelec);

            View item = listaClientes.getChildAt(itemseleccionado);
            if (item != null) item.setBackgroundColor(0);

            itemseleccionado = -1;
        }else {
            Toast.makeText(this, "No se ha seleccionado ningun cliente", Toast.LENGTH_SHORT).show();
        }

    }

    public void eliminarporNombre(String nombre) {
        AdminDB adminDB = new AdminDB(this);
        SQLiteDatabase db = adminDB.getWritableDatabase();

        if (!nombre.isEmpty()){
            int registrosEliminados =
                    db.delete("clientes", "nombre=?", new String[]{nombre});
            db.close();

            if (registrosEliminados > 0) {
                Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontro ese nombre", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "El nombre esta vacio", Toast.LENGTH_SHORT).show();
        }
    }

    //edita con la misma pantalla de agregar pero enviando el cliente seleccionado
    public void editarCliente(View view) {
        if (itemseleccionado >= 0) {
            Clientes clienteSelec = adapterClientes.getItem(itemseleccionado);

            Intent intent = new Intent(this, Activity_AdmClientes.class);
            intent.putExtra("cedula", clienteSelec.getCedula());
            intent.putExtra("nombre", clienteSelec.getNombre());
            intent.putExtra("numero", clienteSelec.getNumero());
            intent.putExtra("genero", clienteSelec.getGenero().toString());
            intent.putExtra("edad", clienteSelec.getEdad());
            intent.putExtra("altura", clienteSelec.getAltura());
            intent.putExtra("peso", clienteSelec.getPeso());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No se ha seleccionado ningun cliente", Toast.LENGTH_SHORT).show();
        }
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

    public void onResume() {
        super.onResume();
        cargarClientes();
    }
}