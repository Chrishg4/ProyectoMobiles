package com.example.proyectomobilesgym;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Activity_AdmAudio extends AppCompatActivity {

    Button btnIniciarGrabacion;
    Button btnDetenerGrabacion;
    Button btnIniciarReproduccion;
    Button btnDetenerReproduccion;
    Button btnGuardarAudio;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private String outputFile;

    private static final int REQUEST_PERMISSION_CODE = 1000;

    public void salir(View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_audio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vincular elementos de la interfaz con sus IDs
        btnIniciarGrabacion = findViewById(R.id.btnIniciarGrabacion);
        btnDetenerGrabacion = findViewById(R.id.btnDetenerGrabacion);
        btnIniciarReproduccion = findViewById(R.id.btnIniciarReproduccion);
        btnDetenerReproduccion = findViewById(R.id.btnDetenerReproduccion);
        btnGuardarAudio = findViewById(R.id.btnGuardarAudio);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_PERMISSION_CODE);
        }

        // Configurar el archivo de salida para la grabación de audio
        outputFile = getExternalFilesDir(null).getAbsolutePath() + "/Grabacion.3gp";
        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();

        // Deshabilitar botones inicialmente
        btnDetenerGrabacion.setEnabled(false);
        btnIniciarReproduccion.setEnabled(false);
        btnDetenerReproduccion.setEnabled(false);
        btnGuardarAudio.setEnabled(false);
    }

    // Método para iniciar la grabación de audio
    public void iniciarGrabacion(View view) {
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Fuente de audio
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Formato de salida
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Codificador de audio
            mediaRecorder.setOutputFile(outputFile); // Archivo de salida
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            isPlaying = false;

            // Actualizar estado de botones
            btnIniciarGrabacion.setEnabled(false);
            btnDetenerGrabacion.setEnabled(true);
            Toast.makeText(this, getString(R.string.toast_start_recordig), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Método para detener la grabación de audio
    public void detenerGrabacion(View view) {
        mediaRecorder.stop(); // Detener grabación
        mediaRecorder.reset(); // Reiniciar el grabador
        isRecording = false;

        // Actualizar estado de botones
        btnIniciarGrabacion.setEnabled(false);
        btnDetenerGrabacion.setEnabled(false);
        btnIniciarReproduccion.setEnabled(true);
        Toast.makeText(this, getString(R.string.toast_stop_recordig), Toast.LENGTH_SHORT).show();
    }

    // Método para iniciar la reproducción de audio
    public void iniciarReproduccion(View view) {
        try {
            mediaPlayer.setDataSource(outputFile); // Configurar fuente de audio
            mediaPlayer.prepare(); // Preparar reproducción
            mediaPlayer.start(); // Iniciar reproducción
            isPlaying = true;

            // Actualizar estado de botones
            btnIniciarReproduccion.setEnabled(false);
            btnDetenerReproduccion.setEnabled(true);
            Toast.makeText(this, getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para detener la reproducción de audio
    public void detenerReproduccion(View view) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop(); // Detener reproducción
                mediaPlayer.reset(); // Reiniciar reproductor
                isPlaying = false;

                // Actualizar estado de botones
                btnDetenerReproduccion.setEnabled(false);
                btnGuardarAudio.setEnabled(true);
                Toast.makeText(this, getString(R.string.toast_stop_playing), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // Metodo para guardar el audio en la base de datos
    public void guardarAudio(View view) {
        // Leer el archivo de audio y convertirlo en un arreglo de bytes
        File audioFile = new File(outputFile); // Ruta del archivo de audio grabado
        byte[] audioData = new byte[(int) audioFile.length()]; // Crear un arreglo de bytes del tamaño del archivo

        try {
            FileInputStream fileInputStream = new FileInputStream(audioFile);
            fileInputStream.read(audioData); // Leer datos de audio en el arreglo
            fileInputStream.close(); // Cerrar el flujo de entrada
        } catch (IOException e) {
            e.printStackTrace(); // Manejar excepción de entrada/salida
            Toast.makeText(this, getString(R.string.toast_error_save_audio), Toast.LENGTH_SHORT).show(); // Notificar al usuario
            return; // Salir del método si hay un error
        }

        Audio audio = new Audio(outputFile);
        audio.setEnBytes(audioData);
        Seleccion.audioSeleccionado = audio;

        finish();
    }
}