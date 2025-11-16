package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity_AdmEntrenadores extends AppCompatActivity {
EditText edNombre, edCedula, edTelefono;
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_entrenadores);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edNombre = findViewById(R.id.edtNombre);
        edCedula = findViewById(R.id.edtCedula);
        edTelefono = findViewById(R.id.edtNumero);
        btn = findViewById(R.id.btnAgregarEditar);

        String id = getIntent().getStringExtra("id");
        if (id != null) {
            // valido los datos de id no sean nulos para cargar la info
            edNombre.setText(getIntent().getStringExtra("nombre"));
            edCedula.setText(getIntent().getStringExtra("id"));
            edTelefono.setText(getIntent().getStringExtra("contacto"));
//            hago que el texto cambie a editar con las varuiables que hay en values
            btn.setText(getString(R.string.btn_edit));
            edCedula.setEnabled(false);
        }

    }
    public void cancelar(View view) {
        finish();
    }

    public void funcionEYG(View view) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getWritableDatabase();
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            // Editar entrenador existente
            ContentValues registro = new ContentValues();
            registro.put("nombre", edNombre.getText().toString());
            registro.put("contacto", edTelefono.getText().toString());


            int filasAfectadas = db.update("entrenadores", registro, "cedula=?", new String[]{id});
            db.close();
            if (filasAfectadas > 0) {
                Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {//aregar un nuevo entrenador
            // Validar que todos los campos esten llenos
            String nombre = edNombre.getText().toString().trim();
            String cedula = edCedula.getText().toString().trim();
            String contacto = edTelefono.getText().toString().trim();

            if (nombre.isEmpty() || cedula.isEmpty() || contacto.isEmpty()) {
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
//            valida que no este registrada la cedula
            if (cedulaExiste(edCedula.getText().toString())) {
                Toast.makeText(this, "La cédula ya está registrada", Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("cedula", cedula);
            registro.put("contacto", contacto);
    db.insert("entrenadores", null, registro);
    db.close();
            Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

//    funcion que me sirve para validar si la cedula ya existe en la base de datos
    private boolean cedulaExiste(String cedula) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT cedula FROM entrenadores WHERE cedula = ?", new String[]{cedula});

        boolean existe = cursor.moveToFirst();

        cursor.close();
        db.close();

        return existe;
    }
}