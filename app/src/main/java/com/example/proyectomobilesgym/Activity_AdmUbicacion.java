package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
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

    EditText txtName, txtLatitud, txtLongitud;

    MapView map;

    IMapController mapController;

    Marker marcador;

    Button btnGuardar, btnCancelar;

    String idOriginal, nombreOriginal;

    double latitudOriginal, longitudOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hay que inicializar la configuración de osmdroid antes de cargar la actividad
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_ubicacion);

        txtName = findViewById(R.id.txtNombre);
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
        GeoPoint utn = new GeoPoint(10.430684188597372, -85.08498580135634);
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
        String id = getIntent().getStringExtra("id");
        if (id != null) {
           idOriginal = getIntent().getStringExtra("id");
           nombreOriginal = getIntent().getStringExtra("nombre");
           latitudOriginal = getIntent().getDoubleExtra("latitud", 0);
           longitudOriginal = getIntent().getDoubleExtra("longitud", 0);

           //cargar datos en los campos
           txtName.setText(nombreOriginal);
            txtLatitud.setText(String.valueOf(latitudOriginal));
            txtLongitud.setText(String.valueOf(longitudOriginal));

           //mover el marcador a la ubicacion guardada
           GeoPoint puntoGuardado = new GeoPoint(latitudOriginal, longitudOriginal);
           marcador.setPosition(puntoGuardado);
           mapController.setCenter(puntoGuardado);
           map.invalidate();

           //remover marcador anterior
           if (marcador != null) {
               map.getOverlays().remove(marcador);
        }
              //aca se crea un nuevo marcador
           marcador = new Marker(map);
           marcador.setPosition(puntoGuardado);
           marcador.setTitle("");
           map.getOverlays().add(marcador);

              //centrar el mapa en el nuevo marcador
              mapController.setCenter(puntoGuardado);
                map.invalidate();

                //cambia el texto del boton guardar
            btnGuardar.setText(getString(R.string.btn_edit));
        }





    }

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

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume(); // necesario para osmdroid
    }

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
        if (idOriginal == null) {
            txtName.setText("");
            txtLatitud.setText("");
            txtLongitud.setText("");
            return;
        } else {
            txtName.setText(nombreOriginal);
            txtLatitud.setText(String.valueOf(latitudOriginal));
            txtLongitud.setText(String.valueOf(longitudOriginal));

        }
    }

    public void guardar(View view) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getWritableDatabase();
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            //editar la ubicacion existente
            ContentValues registro = new ContentValues();
            registro.put("nombre", txtName.getText().toString());
            registro.put("latitud", Double.parseDouble(txtLatitud.getText().toString()));
            registro.put("longitud", Double.parseDouble(txtLongitud.getText().toString()));

            int filasAfectadas = db.update("ubicaciones", registro, "id=?", new String[]{idOriginal});
            db.close();
            if (filasAfectadas > 0) {
                Toast.makeText(this, "Ubicacion actualizada", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {
            // validar que el nombre no exista antes de agregar
            if (validarNombreUbicacion(txtName.getText())) {
                Toast.makeText(this, "El nombre de la ubicacion ya existe", Toast.LENGTH_SHORT).show();
                return;
            }
            //validar que los campos no esten vacios
             if (txtName.getText().toString().isEmpty() ||
                txtLatitud.getText().toString().isEmpty() ||
                txtLongitud.getText().toString().isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }
            // agregar una nueva ubicacion
            ContentValues registro = new ContentValues();
            registro.put("nombre", txtName.getText().toString());
            registro.put("latitud", Double.parseDouble(txtLatitud.getText().toString()));
            registro.put("longitud", Double.parseDouble(txtLongitud.getText().toString()));
            db.insert("ubicaciones", null, registro);
            db.close();
            Toast.makeText(this, "Ubicacion agregada", Toast.LENGTH_SHORT).show();
            finish();


        }


    }

    public boolean validarNombreUbicacion(Editable nombre) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        String[] name = new String[]{nombre.toString()};
        Cursor cursor = db.rawQuery("SELECT nombre FROM ubicaciones WHERE nombre = ?", name);
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;

    }

}