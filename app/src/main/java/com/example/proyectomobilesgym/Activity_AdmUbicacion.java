package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class Activity_AdmUbicacion extends AppCompatActivity implements MapEventsReceiver {
    EditText  txtLatitud, txtLongitud;
    MapView map;
    IMapController mapController;
    Marker marcador;
    Button btnGuardar, btnCancelar;
    double latitudOriginal, longitudOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hay que inicializar la configuración de osmdroid antes de cargar la actividad
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_ubicacion);

        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); // Establezca la fuente de teselas en OpenStreetMap.
        map.setMultiTouchControls(true); // Habilite los controles de multitouch.

        mapController = map.getController(); // obtiene  el controlador del mapa.
        mapController.setZoom(15.0); // establece el zoom inicial

        //cordenadas iniciales UTN
        GeoPoint utn = new GeoPoint(10.430871, -85.084667);
        mapController.setCenter(utn); // establece la ubicación inicial del mapa.

        //currentMarker inicial
        marcador = new Marker(map);
        marcador.setPosition(utn);
        marcador.setTitle("UTN");
        map.getOverlays().add(marcador); // agrega el marcador al mapa.


        txtLatitud.setText(String.valueOf(utn.getLatitude()));// obtiene la latitud del marcador.
        txtLongitud.setText(String.valueOf(utn.getLongitude()));// obtiene la longitud del marcador.

        //overlay para recibir eventos del mapa
        MapEventsOverlay overlay = new MapEventsOverlay(this);
        map.getOverlays().add(overlay);

        //obtener datos del intent

        Intent ubi = getIntent(); // obtiene el intent que inició esta actividad.
        if (ubi  != null && ubi.hasExtra("latitud") && ubi.hasExtra("longitud")) {// verifica si el intent tiene los extras de latitud y longitud.
            latitudOriginal = ubi.getDoubleExtra("latitud", 10.430871);// obtiene la latitud original del intent.
            longitudOriginal = ubi.getDoubleExtra("longitud", -85.084667);
            if (latitudOriginal == 0.0 && longitudOriginal == 0.0) {
                latitudOriginal = 10.430871;
                longitudOriginal = -85.084667;
            }

            txtLatitud.setText(String.valueOf(latitudOriginal));
            txtLongitud.setText(String.valueOf(longitudOriginal));

            //colocar el marcador en la ubicacion seleccionada
            GeoPoint puntoSeleccionado = new GeoPoint(latitudOriginal, longitudOriginal);
            marcador.setPosition(puntoSeleccionado);
            mapController.setCenter(puntoSeleccionado);
            map.invalidate();

            if (marcador != null) {
                map.getOverlays().remove(marcador);
            }
            //aca se crea un nuevo marcador
            marcador = new Marker(map);
            marcador.setPosition(puntoSeleccionado);
            marcador.setTitle("");
            map.getOverlays().add(marcador);

            //centrar el mapa en el nuevo marcador
            mapController.setCenter(puntoSeleccionado);
            map.invalidate();
            //cambia el texto del boton guardar
            btnGuardar.setText(getString(R.string.btn_edit));
        }
    }
    // estos dos metodos son los que manejan los eventos de toque en el mapa
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        ClikEnMapa(p);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        ClikEnMapa(p);
        return true;
    }

    // funcion que maneja el click en el mapa
    public void ClikEnMapa(GeoPoint point) {
        txtLatitud.setText(String.valueOf(point.getLatitude()));
        txtLongitud.setText(String.valueOf(point.getLongitude()));
        marcador.setPosition(point);

        //remover marcador anterior
        if (marcador != null) {
            map.getOverlays().remove(marcador);
        }

        //aca se crea un nuevo marcador
        marcador = new Marker(map);
        marcador.setPosition(point);
        marcador.setTitle("");
        map.getOverlays().add(marcador);

        //centrar el mapa en el nuevo marcador
        mapController.setCenter(point);
        map.invalidate();
    }
    // esto hace que el mapa funcione bien con el ciclo de vida de android
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume(); // necesario para osmdroid
    }
    // el onPause tambien es necesario ya que osmdroid necesita pausar ciertos procesos
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause(); // necesario para osmdroid
    }

    public void cancelar(View view) {
        finish();
    }

    // aca vuelve a traer los campos de la seleccion
    public void reiniciar(View view) {
        if (txtLatitud.getText().toString().isEmpty() || txtLongitud.getText().toString().isEmpty()) {
            txtLatitud.setText("");
            txtLongitud.setText("");
            return;
        } else {
            txtLatitud.setText(String.valueOf(latitudOriginal));
            txtLongitud.setText(String.valueOf(longitudOriginal));

            //vuelve a colocar el marcador en la ubicacion original
            GeoPoint puntoOriginal = new GeoPoint(latitudOriginal, longitudOriginal);
            marcador.setPosition(puntoOriginal);
            mapController.setCenter(puntoOriginal);
            map.invalidate();
        }
    }

    public void guardar(View view) {
        if (txtLatitud.getText().toString().isEmpty() || txtLongitud.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_complete_fields), Toast.LENGTH_SHORT).show();
            return;
        }else {
            double latitud = Double.parseDouble(txtLatitud.getText().toString());
            double longitud = Double.parseDouble(txtLongitud.getText().toString());

            // Guardar en la variable global
            Seleccion.ubicacionSeleccionada = new Ubicaciones(latitud, longitud);

            // Cerrar la Activity
            finish();
        }
    }
}