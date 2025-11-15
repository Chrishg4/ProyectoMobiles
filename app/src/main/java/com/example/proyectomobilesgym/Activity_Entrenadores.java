package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

public class Activity_Entrenadores extends AppCompatActivity {

    EditText edSearch;
    ListView lista;
    CustomAdapter adapter;
    List<Usuarios> datos;
    int itemseleccionado = -1;

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

        datos = new ArrayList<>();
        adapter = new CustomAdapter(this, datos);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            itemseleccionado = position;

            for (int i = 0; i < lista.getChildCount(); i++)
                lista.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

            view.setBackgroundColor(Color.LTGRAY);
        });

//        insertarDatosPrueba(); descomentar para insertar datos de prueba
        cargarEntrenadores();
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

            View item = lista.getChildAt(itemseleccionado);
            if (item != null) item.setBackgroundColor(0);

            itemseleccionado = -1;
        } else {
            Toast.makeText(this, "Debe seleccionar un item", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Eliminado correctamente", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "No se encontró ese nombre", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Falta el nombre para eliminar", Toast.LENGTH_LONG).show();
        }
    }

    public void editar(View view) {
        if (itemseleccionado >= 0) {

            Usuarios u = adapter.getItem(itemseleccionado);

            Intent intent = new Intent(this, Activity_AdmEntrenadores.class);
            intent.putExtra("id", u.getId());
            intent.putExtra("nombre", u.getTxtPrincipal());
            intent.putExtra("contacto", u.getTxtSecundario());
            startActivity(intent);

        } else {
            Toast.makeText(this, "Debe seleccionar un item", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertarDatosPrueba() {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("cedula", "201");
        cv.put("nombre", "Entrenador Demo 1");
        cv.put("contacto", "7000-0000");
        db.insert("entrenadores", null, cv);

        cv.clear();
        cv.put("cedula", "202");
        cv.put("nombre", "Entrenador Demo 2");
        cv.put("contacto", "7111-1111");
        db.insert("entrenadores", null, cv);

        db.close();
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
                        R.drawable.ic_launcher_foreground,
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


//    es5to recarga la lista al volver de agregar o editar porque se usa el finish y no se crea un nuevo intent

    protected void onResume() {
        super.onResume();
        cargarEntrenadores();
    }

}
