package com.example.proyectomobilesgym;

import android.content.ContentValues;
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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Activity_Entrenadores extends AppCompatActivity {

    EditText edSearch;
    ListView lista;
    CustomAdapter adapter;
    List<Usuarios> datos;
    int itemseleccionado = -1;

    Button btnAgregar, btnEliminar, btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entrenadores);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edSearch = findViewById(R.id.edtBuscar);
        lista = findViewById(R.id.listEntrenadores);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnEditar = findViewById(R.id.btnEditar);

        deshabilitarBotones();





        datos = new ArrayList<>();
        adapter = new CustomAdapter(this, datos);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            itemseleccionado = position;

            for (int i = 0; i < lista.getChildCount(); i++)
                lista.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

            view.setBackgroundColor(Color.LTGRAY);
            //habilito los botones de editar y eliminar
            if (itemseleccionado >= 0) {
                int colorRojo = ContextCompat.getColor(this, R.color.rojoUTN);
                int colorNaranja = ContextCompat.getColor(this, R.color.naranjaUTN);
                btnEliminar.setBackgroundTintList(ColorStateList.valueOf(colorRojo));
                btnEditar.setBackgroundTintList(ColorStateList.valueOf(colorNaranja));
            }

        });
        cargarEntrenadores();

        // buscar cliente al escribir en el editText
        edSearch.addTextChangedListener(new TextWatcher() {
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


    }

    public void deshabilitarBotones() {
        btnEliminar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        btnEditar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
    }
    public void buscarCliente() {

        String nombreBuscado = edSearch.getText().toString().trim();// obtener el texto del EditText y eliminar espacios en blanco al inicio y al final
        if (nombreBuscado.isEmpty()) {
            cargarEntrenadores();// si el texto esta vacio cargar todos los clientes
            deshabilitarBotones();
            itemseleccionado = -1;
//        quitar el color de seleccion de los items
            for (int i = 0; i < lista.getChildCount(); i++)
                lista.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);


            return;
        }
        AdminDB adminDB = new AdminDB(this);
        SQLiteDatabase db = adminDB.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT cedula, nombre, contacto FROM entrenadores WHERE nombre LIKE ? OR cedula LIKE ?",
                new String[]{"%" + nombreBuscado + "%" , "%" + nombreBuscado + "%"}
        );
        datos.clear();
        if (cursor.moveToFirst()) {
            do {

                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String contacto = cursor.getString(2);

                Usuarios u = new Usuarios(
                        id,
                        R.drawable.entrenadores,
                        nombre,
                        contacto
                );

                datos.add(u);

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
        deshabilitarBotones();
        itemseleccionado = -1;

//        quitar el color de seleccion de los items
        for (int i = 0; i < lista.getChildCount(); i++)
            lista.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
    }
    public void agregar(View view) {
        Intent intent = new Intent(this, Activity_AdmEntrenadores.class);
        startActivity(intent);
    }

    public void Eliminar(View view) {

        if (itemseleccionado >= 0) {

            // obtiene el objeto seleccionado
            Usuarios u = adapter.getItem(itemseleccionado);
            // elimino en db usando el nombre
            EliminarPorNombre(u.getTxtPrincipal());
            // elimino del adapter, tuve que crear un metodo remove en CustomAdapter porque no existia
            adapter.remove(u);
            itemseleccionado = -1;

            deshabilitarBotones();

            View item = lista.getChildAt(itemseleccionado);
            if (item != null) item.setBackgroundColor(0);

            itemseleccionado = -1;
            edSearch.setText("");
        } else {
            Toast.makeText(this, getString(R.string.toast_select_trainer), Toast.LENGTH_SHORT).show();
        }
    }

        public void EliminarPorNombre(String nombre) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();

        if (!nombre.isEmpty()) {
            int registrosEliminados =
                    BaseDatos.delete("entrenadores", "nombre=?", new String[]{nombre});

            BaseDatos.close();

            if (registrosEliminados > 0)
                Toast.makeText(this, getString(R.string.toast_deleted_success), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, getString(R.string.toast_name_not_found), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, getString(R.string.toast_missing_name), Toast.LENGTH_LONG).show();
        }
    }

    public void editar(View view) {
        if (itemseleccionado >= 0) {

            Usuarios u = adapter.getItem(itemseleccionado);

            Intent intent = new Intent(this, Activity_AdmEntrenadores.class);
            intent.putExtra("id", u.getId());
            intent.putExtra("nombre", u.getTxtPrincipal());
            intent.putExtra("contacto", u.getTxtSecundario());
            Audio audio = cargarAudio(u.getId());
            if (audio.getEnBytes() != null && audio.getEnBytes().length > 0) {
                intent.putExtra("audio", audio.getEnBytes());
            }
            Imagen imagen = cargarImagen(u.getId());
            if (imagen.getImagenEnBytes() != null && imagen.getImagenEnBytes().length > 0) {
                intent.putExtra("imagen", imagen.getImagenEnBytes());
            }
            Ubicaciones ubicacion = cargarUbicaciones(u.getId());
            if (ubicacion.getLatitud() != 0 && ubicacion.getLongitud() != 0) {
                intent.putExtra("latitud", ubicacion.getLatitud());
                intent.putExtra("longitud", ubicacion.getLongitud());
            }
            startActivity(intent);

        } else {
            Toast.makeText(this, getString(R.string.toast_select_trainer), Toast.LENGTH_SHORT).show();
        }
    }

    private Audio cargarAudio(String id) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT audio FROM entrenadores WHERE cedula=?",
                new String[]{id}
        );
        Audio audio = new Audio(getExternalFilesDir(null).getAbsolutePath() + "/Grabacion.3gp");
        if (cursor.moveToFirst()) {
            do {
                byte[] audioBytes = cursor.getBlob(0);
                audio.setEnBytes(audioBytes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return audio;
    }

    private Imagen cargarImagen(String id) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT imagen FROM entrenadores WHERE cedula=?",
                new String[]{id}
        );
        Imagen imagen;
        if (cursor.moveToFirst()) {
            do {
                byte[] imagenBytes = cursor.getBlob(0);
                imagen = new Imagen(imagenBytes);
            } while (cursor.moveToNext());
        } else {
            imagen = null;
            }
        cursor.close();
        db.close();
        return imagen;
    }

    private void cargarEntrenadores() {

        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT cedula, nombre, contacto FROM entrenadores",
                null
        );

        datos.clear();

        if (cursor.moveToFirst()) {
            do {

                String id = cursor.getString(0);
                String nombre = cursor.getString(1);
                String contacto = cursor.getString(2);

                Usuarios u = new Usuarios(
                        id,
                        R.drawable.entrenadores,
                        nombre,
                        contacto
                );

                datos.add(u);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        adapter.notifyDataSetChanged();
    }

    public Ubicaciones cargarUbicaciones(String id) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT latitud, longitud FROM entrenadores WHERE cedula=?",
                new String[]{id}
        );

        Ubicaciones ubicacion;

        if (cursor.moveToFirst()) {
            do {
                double latitud = cursor.getDouble(0);
                double longitud = cursor.getDouble(1);
                ubicacion = new Ubicaciones(
                        latitud,
                        longitud
                );
            } while (cursor.moveToNext());
        } else {
            ubicacion = new Ubicaciones(0,0);
        }
        cursor.close();
        db.close();
        return ubicacion;
    }



//    es5to recarga la lista al volver de agregar o editar porque se usa el finish y no se crea un nuevo intent

    protected void onResume() {
        super.onResume();
        cargarEntrenadores();
        edSearch.setText("");
    }

    public void exit(View view) {
        finish();
    }

}
