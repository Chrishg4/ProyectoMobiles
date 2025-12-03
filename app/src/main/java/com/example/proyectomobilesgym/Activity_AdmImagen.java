package com.example.proyectomobilesgym;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;

public class Activity_AdmImagen extends AppCompatActivity {
    private ActivityResultLauncher<Intent> lanzadorTomarFoto;
    private Bitmap imagenBitmap;
    private ImageView vistaImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_imagen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        vistaImagen = findViewById(R.id.vistaImagen);

        // Inicializar el lanzador para tomar foto
        lanzadorTomarFoto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                resultado -> {
                    if (resultado.getResultCode() == RESULT_OK) {
                        imagenBitmap = (Bitmap) resultado.getData().getExtras().get("data");
                        vistaImagen.setImageBitmap(imagenBitmap);
                    }
                }
        );
    }

    // Metodo para tomar foto
    public void tomarFoto(View vista) {
        Intent intentTomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        lanzadorTomarFoto.launch(intentTomarFoto);
    }
    // esta funcion me sirve para pasar de Bitmap a Bytes
    private byte[] convertir(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // guardar la imagen en la clase Seleccion
    public void guardarImagen(View vista) {
        if (imagenBitmap != null) {
            byte[] imagenBytes = convertir(imagenBitmap);

            // Guardar en la variable global
            Seleccion.imagenSeleccionada = new Imagen(imagenBytes);

            // Cerrar la Activity
            finish();
        }
    }
    // salir sin guardar imagen y verifico que siempre la imagen sea nula
    public void salir(View vista) {
        Seleccion.imagenSeleccionada = null;
        finish();
    }
}
