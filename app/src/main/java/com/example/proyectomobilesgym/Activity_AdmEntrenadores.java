package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class Activity_AdmEntrenadores extends AppCompatActivity {
EditText edNombre, edCedula, edTelefono;
Button btn, btnCancelar, btnReiniciar, btnAudio, btnGrabar;
MediaPlayer reproductor;
Audio audio = null, audioOriginal = null;
boolean hayAudio = false;

String nombreOriginal, cedulaOriginal, telefonoOriginal;
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
        btn = findViewById(R.id.btnAgregarEditarUbi);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnReiniciar = findViewById(R.id.Reiniciar);

        reproductor = new MediaPlayer();
        audio = new Audio(getExternalFilesDir(null).getAbsolutePath() + "/Grabacion.3gp");

        String id = getIntent().getStringExtra("id");
        if (id != null) {

            cedulaOriginal = getIntent().getStringExtra("id");
            nombreOriginal = getIntent().getStringExtra("nombre");
            telefonoOriginal = getIntent().getStringExtra("contacto");
            audio.setEnBytes(getIntent().getByteArrayExtra("audio"));
            audioOriginal = new Audio(audio.getRuta());
            audioOriginal.setEnBytes(Arrays.copyOf(audio.getEnBytes(), audio.getEnBytes().length));
            prepararAudio();

            edCedula.setText(cedulaOriginal);
            edNombre.setText(nombreOriginal);
            edTelefono.setText(telefonoOriginal);


//            hago que el texto cambie a editar con las varuiables que hay en values
            btn.setText(getString(R.string.btn_edit));
            edCedula.setEnabled(false);
        }

    }

    public void irAGrabarAudio(View view) {
        Seleccion.audioSeleccionado = null;
        Intent intent = new Intent(this, Activity_AdmAudio.class);
        if (audio.getEnBytes() != null && audio.getEnBytes().length > 0){
            intent.putExtra("audio", audio.getEnBytes());
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Seleccion.audioSeleccionado != null){
            hayAudio = true;
            audio = Seleccion.audioSeleccionado;
            prepararAudio();
        }
    }

    private void prepararAudio() {
        if (audio.getEnBytes() == null || audio.getEnBytes().length == 0){
            hayAudio = false;
            return;
        }
        try {
            File tempFile = new File(audio.getRuta());

            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(audio.getEnBytes());
            fos.close();

            reproductor.reset();
            reproductor.setDataSource(tempFile.getAbsolutePath());
            reproductor.prepare();
            hayAudio = true;

            Seleccion.audioSeleccionado = null;

        } catch (Exception e) {
            e.printStackTrace();
            hayAudio = false;
            Toast.makeText(this, getString(R.string.toast_audio_reading_error), Toast.LENGTH_SHORT).show();
        }
    }


    public void reproducirAudio(View view) {
        if (hayAudio){
            reproductor.start();
        } else {
            Toast.makeText(this, getString(R.string.toast_no_audio), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelar(View view) {
        finish();
    }

    public void reiniciar (View view) {
        if(cedulaOriginal == null){
            edNombre.setText("");
            edCedula.setText("");
            edTelefono.setText("");
            hayAudio = false;
            audio = new Audio(getExternalFilesDir(null).getAbsolutePath() + "/Grabacion.3gp");

        } else {
            edNombre.setText(nombreOriginal);
            edCedula.setText(cedulaOriginal);
            edTelefono.setText(telefonoOriginal);
            hayAudio = true;
            audio = audioOriginal;
            prepararAudio();
        }

    }

    public void funcionEYG(View view) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getWritableDatabase();
        String id = getIntent().getStringExtra("id");
        // Editar entrenador existente
        if (id != null) {


            // Validar que todos los campos estén llenos
            String nombre = edNombre.getText().toString().trim();
            String contacto = edTelefono.getText().toString().trim();

            if (nombre.isEmpty() || contacto.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_complete_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("contacto", contacto);
            if (hayAudio) {
                registro.put("audio", audio.getEnBytes());
            }

            int filasAfectadas = db.update("entrenadores", registro, "cedula=?", new String[]{id});
            db.close();
            if (filasAfectadas > 0) {
                Toast.makeText(this, getString(R.string.toast_registered), Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {//aregar un nuevo entrenador
            // Validar que todos los campos esten llenos
            String nombre = edNombre.getText().toString().trim();
            String cedula = edCedula.getText().toString().trim();
            String contacto = edTelefono.getText().toString().trim();

            if (nombre.isEmpty() || cedula.isEmpty() || contacto.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_complete_fields), Toast.LENGTH_SHORT).show();
                return;
            }
//            valida que no este registrada la cedula
            if (cedulaExiste(edCedula.getText().toString())) {
                Toast.makeText(this, getString(R.string.toast_id_exists), Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("cedula", cedula);
            registro.put("contacto", contacto);
            if (hayAudio){
                registro.put("audio", audio.getEnBytes());
            }
    db.insert("entrenadores", null, registro);
    db.close();
            Toast.makeText(this, getString(R.string.toast_registered), Toast.LENGTH_SHORT).show();
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