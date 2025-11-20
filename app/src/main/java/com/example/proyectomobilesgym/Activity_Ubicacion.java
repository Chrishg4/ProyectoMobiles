package com.example.proyectomobilesgym;

import android.content.Intent;
import android.content.res.ColorStateList;
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

public class Activity_Ubicacion extends AppCompatActivity {

    EditText BuscarUbicacion;

    ListView listaUbicaciones;

    UbicacionesAdapter adapterUbicaciones;

    List<Ubicaciones> datosUbicaciones;

    int itemseleccionado = -1;

    Button btnAgregarUbicacion, btnEliminarUbicacion, btnEditarUbicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ubicacion);

        BuscarUbicacion = findViewById(R.id.Buscar);
        listaUbicaciones = findViewById(R.id.listUbicacion);
        btnAgregarUbicacion = findViewById(R.id.btnAgregar);
        btnEliminarUbicacion = findViewById(R.id.btnEliminar);
        btnEditarUbicacion = findViewById(R.id.btnEditar);

        datosUbicaciones = new ArrayList<>();
        adapterUbicaciones = new UbicacionesAdapter(this, datosUbicaciones);
        listaUbicaciones.setAdapter(adapterUbicaciones);

        listaUbicaciones.setOnItemClickListener((parent, view, position, id) -> {
            itemseleccionado = position;

            for (int i = 0; i < listaUbicaciones.getChildCount(); i++)
                listaUbicaciones.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

            view.setBackgroundColor(Color.LTGRAY);

            // habilitar botones de editar y eliminar al seleccionar un cliente
            //btnEliminarUbicacion.setEnabled(true);
            btnEliminarUbicacion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D33232")));
            //btnEditarUbicacion.setEnabled(true);
            btnEditarUbicacion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));
        });

        BuscarUbicacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarUbicacion();
            }
        });
    }

    public void agregarUbicacion (View view){
        Intent intent = new Intent(this, Activity_AdmUbicacion.class);
        startActivity(intent);
    }

    public void deshabilitarBotones() {
        //btnEliminarCliente.setEnabled(false);
        if (itemseleccionado < 0) {
            btnEliminarUbicacion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
            //btnEditarCliente.setEnabled(false);
            btnEditarUbicacion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        }
    }

    public void fondoTransparenteListView(View view) {
        for (int i = 0; i < listaUbicaciones.getChildCount(); i++) {
            listaUbicaciones.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void atras(View view) {
        finish();

    }

    //funcion para buscar por nombre en el editText BuscarCliente y actualizar el listView automaticamente
    public void buscarUbicacion() {

        String textoBusqueda = BuscarUbicacion.getText().toString().trim();// obtener el texto del EditText y eliminar espacios en blanco al inicio y al final
        if (textoBusqueda.isEmpty()) {
            cargarUbicaciones(); // si el texto de búsqueda está vacío, cargar todas las ubicaciones
            itemseleccionado = -1;
            deshabilitarBotones();
            fondoTransparenteListView(listaUbicaciones);
            return;
        }
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, latitud, longitud FROM ubicaciones WHERE nombre LIKE ?",
                new String[]{"%" + textoBusqueda + "%"}
        );
        datosUbicaciones.clear();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                double latitud = cursor.getDouble(2);
                double longitud = cursor.getDouble(3);

                Ubicaciones ubicacion = new Ubicaciones(
                        id,
                        nombre,
                        latitud,
                        longitud
                );
                datosUbicaciones.add(ubicacion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        adapterUbicaciones.notifyDataSetChanged();
        itemseleccionado = -1;
        deshabilitarBotones();
        fondoTransparenteListView(listaUbicaciones);
    }


    public void editarUbicacion (View view){
        if (itemseleccionado >= 0){
            Ubicaciones ubicacionSeleccionada = datosUbicaciones.get(itemseleccionado);

            Intent intent = new Intent(this, Activity_AdmUbicacion.class);
            intent.putExtra("id", ubicacionSeleccionada.getId());
            intent.putExtra("nombre", ubicacionSeleccionada.getNombre());
            intent.putExtra("latitud", ubicacionSeleccionada.getLatitud());
            intent.putExtra("longitud", ubicacionSeleccionada.getLongitud());
            startActivity(intent);
        }else {
            Toast.makeText(this , "Seleccione una ubicacion para editar", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarUbicacion (View view){
        if (itemseleccionado >= 0){
            Ubicaciones ubicacionSeleccionada = adapterUbicaciones.getItem(itemseleccionado);

            eliminarporNombre(ubicacionSeleccionada.getNombre());

            adapterUbicaciones.remove(ubicacionSeleccionada);

            View item = listaUbicaciones.getChildAt(itemseleccionado);
            if (item != null)item.setBackgroundColor(0);

            itemseleccionado = -1;
            deshabilitarBotones();
        }else {
            Toast.makeText(this , "Seleccione una ubicacion para eliminar", Toast.LENGTH_SHORT).show();
        }

    }

    public void eliminarporNombre(String nombre) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        if (!nombre.isEmpty()){
            int cantidadEliminada =
                    db.delete("ubicaciones", "nombre = ?", new String[]{nombre});
            db.close();

            if (cantidadEliminada > 0) {
                Toast.makeText(this, "Ubicacion eliminada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontro la ubicacion", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show();
        }
    }

    public void cargarUbicaciones (){
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        //cargar las ubicaciones desde la base de datos
        Cursor cursor = db.rawQuery(
                "SELECT id, nombre, latitud, longitud FROM ubicaciones", null
        );

        datosUbicaciones.clear();

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                double latitud = cursor.getDouble(2);
                double longitud = cursor.getDouble(3);

                Ubicaciones ubicacion = new Ubicaciones(
                        id,
                        nombre,
                        latitud,
                        longitud
                );
                datosUbicaciones.add(ubicacion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        adapterUbicaciones.notifyDataSetChanged();

    }

    public void onResume() {
        super.onResume();
        cargarUbicaciones();
        BuscarUbicacion.setText("");
    }


}