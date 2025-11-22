package com.example.proyectomobilesgym;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity_AdmAudio extends AppCompatActivity {

    Button btnIniciarGrabacion, btnDetenerGrabacion, btnIniciarReproduccion,
            btnDetenerReproduccion, btnGuardarAudio;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean hayAudio = false;

    private String outputFile;
    private Audio audio;

    private static final int REQUEST_PERMISSION_CODE = 1000;

    public void salir(View view) {
        if (audio != null) {
            Seleccion.audioSeleccionado = audio;
        } else {
            Seleccion.audioSeleccionado = new Audio(outputFile);
        }
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

        outputFile = getExternalFilesDir(null).getAbsolutePath() + "/Grabacion.3gp";

        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();

        actualizarColorBoton(btnDetenerGrabacion, false, "#BDBDBD");
        actualizarColorBoton(btnIniciarReproduccion, false, "#BDBDBD");
        actualizarColorBoton(btnDetenerReproduccion, false, "#BDBDBD");
        actualizarColorBoton(btnGuardarAudio, false, "#BDBDBD");


        // Si viene audio desde el Intent, cargarlo
        byte[] audioData = getIntent().getByteArrayExtra("audio");
        if (audioData != null && audioData.length > 0) {
            audio = new Audio(outputFile);
            audio.setEnBytes(audioData);
            prepararAudio();
        }
    }

    private void prepararAudio() {
        try {
            File tempFile = new File(audio.getRuta());

            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(audio.getEnBytes());
            fos.close();

            mediaPlayer.reset();  // *** CAMBIO ***
            mediaPlayer.setDataSource(tempFile.getAbsolutePath());
            mediaPlayer.prepare();

            hayAudio = true;
            actualizarColorBoton(btnIniciarReproduccion, true, "#2196F3");

        } catch (Exception e) {
            e.printStackTrace();
            hayAudio = false;
            Toast.makeText(this, "Error al convertir el audio", Toast.LENGTH_SHORT).show();
        }
    }

    public void iniciarGrabacion(View view) {
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);

            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;
            isPlaying = false;

            actualizarColorBoton(btnIniciarGrabacion, false, "#BDBDBD");
            actualizarColorBoton(btnDetenerGrabacion, true, "#F44336");

            Toast.makeText(this, getString(R.string.toast_start_recordig), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void detenerGrabacion(View view) {
        mediaRecorder.stop();
        mediaRecorder.reset();

        isRecording = false;

        actualizarColorBoton(btnIniciarGrabacion, false, "#BDBDBD");
        actualizarColorBoton(btnDetenerGrabacion, false, "#BDBDBD");
        actualizarColorBoton(btnIniciarReproduccion, true, "#2196F3");

        Toast.makeText(this, getString(R.string.toast_stop_recordig), Toast.LENGTH_SHORT).show();
    }

    public void iniciarReproduccion(View view) {
        try {
            mediaPlayer.reset();   // *** CAMBIO CRÍTICO ***

            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();

            isPlaying = true;

            actualizarColorBoton(btnIniciarReproduccion, false, "#BDBDBD");
            actualizarColorBoton(btnDetenerReproduccion, true, "#F44336");


            Toast.makeText(this, getString(R.string.toast_start_playing), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void detenerReproduccion(View view) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                isPlaying = false;

                actualizarColorBoton(btnDetenerReproduccion, false, "#BDBDBD");
                actualizarColorBoton(btnGuardarAudio, true, "#4CAF50");


                Toast.makeText(this, getString(R.string.toast_stop_playing), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarAudio(View view) {
        File audioFile = new File(outputFile);
        byte[] audioData = new byte[(int) audioFile.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(audioFile);
            fileInputStream.read(audioData);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.toast_error_save_audio), Toast.LENGTH_SHORT).show();
            return;
        }

        Audio audio = new Audio(outputFile);
        audio.setEnBytes(audioData);
        Seleccion.audioSeleccionado = audio;

        finish();
    }

    private void actualizarColorBoton(Button boton, boolean habilitado, String colorOriginal) {
        if (habilitado) {
            boton.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor(colorOriginal))
            );
            boton.setEnabled(true);
        } else {
            boton.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#BDBDBD"))
            );
            boton.setEnabled(false);
        }
    }

}
