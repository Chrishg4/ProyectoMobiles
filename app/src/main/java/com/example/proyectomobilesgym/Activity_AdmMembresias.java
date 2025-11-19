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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_AdmMembresias extends AppCompatActivity {
    private boolean editar = false;
    private Servicio enEdicionServicio = null;
    private Membresia enEdicionMembresia = null;
    private AutoCompleteTextView edtCedula, edtCedulaEntrenador, edtCodigoServicio;
    private EditText edtNombreCliente;
    private EditText edtNombreEntrenador;
    private Spinner spTipo;
    private EditText edtNombreServicio, edtPrecioServicio, edtPrecioTotal;
    private Button btnAgregarServicio, btnEditarServicio, btnEliminarServicio;
    private ListView listaView;
    private int itemseleccionado;
    private Button btnGuardar, btnReiniciar, btnCancelar;
    private CustomAdapterServicio adaptador;
    private AdminDB admin;
    private DAOMembresia membresiaDB;
    private DAOServicio servicioDB;
    private ListServicio lista;
    private ArrayAdapter<String> adapterCedulas,adapterCedulasEntrenadores;
    private ArrayAdapter<Integer> adapterCodigos;
    private List<String> cedulas, cedulasEntrenadores;
    private List<Integer> codigos;

    public void guardar(View view) {
        if (edtNombreCliente.getText().toString().isEmpty() || edtNombreEntrenador.getText().toString().isEmpty() || lista == null || lista.size() == 0) {
            Toast.makeText(this, R.string.toast_fields_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if (enEdicionMembresia != null){
            int codigo = enEdicionMembresia.getCodigo();

            int posicionTipo = spTipo.getSelectedItemPosition();
            if (posicionTipo < 0 || posicionTipo >= TipoMembresia.values().length) {
                Toast.makeText(this, R.string.toast_type_required, Toast.LENGTH_SHORT).show();
                return;
            }
            TipoMembresia tipo = TipoMembresia.values()[posicionTipo];

            String cedulaCliente = edtCedula.getText().toString().trim();
            String cedulaEntrenador = edtCedulaEntrenador.getText().toString().trim();
            double precio = 0.0;
            String precioTxt = edtPrecioTotal.getText().toString().trim();
            if (!precioTxt.isEmpty()) {
                precio = Double.parseDouble(precioTxt);
            }
            Membresia membresia = new Membresia(
                    codigo,
                    tipo,
                    precio,
                    cedulaCliente,
                    cedulaEntrenador,
                    lista
            );
            membresiaDB.actualizar(membresia);
            finish();

        }

        double precioTotal = 0.0;
        String precioTexto = edtPrecioTotal.getText().toString().trim();

        if (!precioTexto.isEmpty()) {
            precioTotal = Double.parseDouble(precioTexto);
        }

        TipoMembresia tipo = TipoMembresia.values()[spTipo.getSelectedItemPosition()];

        if (lista == null) {
            lista = new ListServicio();
        }

        Membresia membresia = new Membresia(
                -1,
                tipo,
                precioTotal,
                edtCedula.getText().toString(),
                edtCedulaEntrenador.getText().toString(),
                lista
        );
        membresiaDB.insertar(membresia);
        finish();
    }
    public void reiniciar(View view) {
        if (enEdicionMembresia != null){
            int codigo = enEdicionMembresia.getCodigo();
            String cedulaCliente = enEdicionMembresia.getCliente();
            String cedulaEntrenador = enEdicionMembresia.getEntrenador();
            TipoMembresia tipo = enEdicionMembresia.getTipo();
            String nombreCliente = cargarNombre(cedulaCliente, "clientes");
            String nombreEntrenador = cargarNombre(cedulaEntrenador, "entrenadores");
            cargarMembresia(codigo, cedulaCliente, cedulaEntrenador, nombreCliente, nombreEntrenador, tipo);
            calcularPrecioTotal();
        }
    }

    public void agregarServicio(View view) {
        if (edtNombreServicio.getText().toString().isEmpty() || edtPrecioServicio.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_fields_service_required), Toast.LENGTH_SHORT).show();
            return;
        }
        int codigo;
        Servicio servicio = new Servicio(
                -1,
                edtNombreServicio.getText().toString(),
                Double.parseDouble(edtPrecioServicio.getText().toString())
        );
        if (editar){
            codigo = enEdicionServicio.getCodigo();
            servicio.setCodigo(codigo);
            servicioDB.actualizar(servicio);
            editar = false;
        } else {
            codigo= (int) servicioDB.insertar(servicio);
        }
        servicio.setCodigo(codigo);
        lista.add(servicio);
        adaptador.notifyDataSetChanged();
        edtCodigoServicio.setText("");
        edtNombreServicio.setText("");
        edtPrecioServicio.setText("");
        setItemSeleccionado(-1);
        enEdicionServicio = null;
    }

    public void editarServicio(View view) {
        if (editar) {
            editar = false;
            edtCodigoServicio.setText("");
            edtNombreServicio.setText("");
            edtPrecioServicio.setText("");
            setItemSeleccionado(-1);

            if (enEdicionServicio == null) return;
            lista.add(enEdicionServicio);
            adaptador.notifyDataSetChanged();
            enEdicionServicio = null;
            return;
        }
        if (itemseleccionado == -1){
            Toast.makeText(this, getString(R.string.toast_no_service_selected), Toast.LENGTH_SHORT).show();
            return;
        }
        Servicio servicio = lista.get(itemseleccionado);
        adaptador.remove(itemseleccionado);
        edtCodigoServicio.setText(String.valueOf(servicio.getCodigo()));
        edtNombreServicio.setText(servicio.getNombre());
        edtPrecioServicio.setText(String.valueOf(servicio.getPrecio()));
        setItemSeleccionado(-1);
        editar = true;
        enEdicionServicio = servicio;
    }

    public void eliminarServicio(View view) {
        if (itemseleccionado == -1){
            Toast.makeText(this, getString(R.string.toast_no_service_selected), Toast.LENGTH_SHORT).show();
            return;
        }
        adaptador.remove(itemseleccionado);
        setItemSeleccionado(-1);
    }

    public void irASeleccionarServicio(View view) {
        Seleccion.servicioSeleccionado = null;
        Intent intent = new Intent(this, Activity_SelectServicios.class);
        startActivity(intent);
    }

    public void irASeleccionarEntrenador(View view) {
        Seleccion.entrenadorSeleccionado = null;
        Intent intent = new Intent(this, Activity_SelectEntrenadores.class);
        startActivity(intent);
    }

    public void irASeleccionarCliente(View view) {
        Seleccion.clienteSeleccionado = null;
        Intent intent = new Intent(this, Activity_SelectClientes.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Seleccion.servicioSeleccionado != null){
            lista.add(Seleccion.servicioSeleccionado);
            adaptador.notifyDataSetChanged();
            calcularPrecioTotal();
            Seleccion.servicioSeleccionado = null;
        }
        if (Seleccion.entrenadorSeleccionado != null) {
            Usuarios entrenador = Seleccion.entrenadorSeleccionado;
            edtCedulaEntrenador.setText(entrenador.getId());
            edtNombreEntrenador.setText(entrenador.getTxtPrincipal());
            Seleccion.entrenadorSeleccionado = null;
        }
        if (Seleccion.clienteSeleccionado != null){
            Clientes cliente = Seleccion.clienteSeleccionado;
            edtCedula.setText(cliente.getCedula());
            edtNombreCliente.setText(cliente.getNombre());
            Seleccion.clienteSeleccionado = null;
        }
    }


    public void salir(View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_membresias);

        edtCedula = findViewById(R.id.edtCedula);
        edtNombreCliente = findViewById(R.id.edtNombreCliente);
        edtNombreCliente.setEnabled(false);

        edtCedulaEntrenador = findViewById(R.id.edtCedulaEntrenador);
        edtNombreEntrenador = findViewById(R.id.edtNombreEntrenador);
        edtNombreEntrenador.setEnabled(false);

        spTipo = findViewById(R.id.spTipo);

        edtCodigoServicio = findViewById(R.id.edtCodigoServicio);
        edtNombreServicio = findViewById(R.id.edtNombreServicio);
        edtNombreServicio.setEnabled(true);
        edtPrecioServicio = findViewById(R.id.edtPrecioServicio);
        edtPrecioTotal = findViewById(R.id.edtPrecioTotal);
        edtPrecioTotal.setEnabled(false);


        btnAgregarServicio = findViewById(R.id.btnAgregarServicio);
        btnEditarServicio = findViewById(R.id.btnEditarServicio);
        btnEliminarServicio = findViewById(R.id.btnEliminarServicio);
        setItemSeleccionado(-1);

        listaView = findViewById(R.id.listServicios);

        // Botones generales de la pantalla
        btnGuardar = findViewById(R.id.btnGuardar);
        btnReiniciar = findViewById(R.id.btnSalir);
        btnCancelar = findViewById(R.id.btnCancelar);

        ArrayAdapter<TipoMembresia> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TipoMembresia.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);

        admin = new AdminDB(this);
        membresiaDB = new DAOMembresia(admin.getWritableDatabase());
        servicioDB = new DAOServicio(admin.getWritableDatabase());

        lista = new ListServicio();
        adaptador = new CustomAdapterServicio(this, lista);
        listaView.setAdapter(adaptador);

        int codigo = getIntent().getIntExtra("codigo", -1);
        if (codigo != -1){
            String cedulaCliente = getIntent().getStringExtra("cliente");
            String cedulaEntrenador = getIntent().getStringExtra("entrenador");
            TipoMembresia tipo = (TipoMembresia) getIntent().getSerializableExtra("tipo");
            String nombreCliente = cargarNombre(cedulaCliente, "clientes");
            String nombreEntrenador = cargarNombre(cedulaEntrenador, "entrenadores");

            cargarMembresia(codigo, cedulaCliente, cedulaEntrenador, nombreCliente, nombreEntrenador, tipo);
            calcularPrecioTotal();

            enEdicionMembresia = new Membresia(codigo, tipo, lista.getPrecioTotal(), cedulaCliente, cedulaEntrenador, servicioDB.buscarPorMembresia(codigo));
        }

        listaView.setOnItemClickListener(
                (parent, view, position, id) -> {
                    setItemSeleccionado(position);
                    for (int i = 0; i < listaView.getChildCount(); i++)
                        listaView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(Color.LTGRAY);
                }
        );

        cedulas = obtenerCedulas("clientes");
        cedulasEntrenadores = obtenerCedulas("entrenadores");
        codigos = obtenerCodigos();

        adapterCedulas = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cedulas
        );
        adapterCedulasEntrenadores = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cedulasEntrenadores
        );
        adapterCodigos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                codigos
        );

        edtCedula.setAdapter(adapterCedulas);
        edtCedulaEntrenador.setAdapter(adapterCedulasEntrenadores);
        edtCodigoServicio.setAdapter(adapterCodigos);


        edtCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> nuevasCedulas = buscarCedulaPor(edtCedula.getText().toString(), "clientes");
                adapterCedulas.clear();
                adapterCedulas.addAll(nuevasCedulas);
                adapterCedulas.notifyDataSetChanged();
                if (!edtCedula.isPopupShowing()) {
                    edtCedula.showDropDown();
                }
                edtNombreCliente.setText("");
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        edtCedulaEntrenador.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> nuevasCedulas = buscarCedulaPor(edtCedula.getText().toString(), "entrenadores");
                adapterCedulasEntrenadores.clear();
                adapterCedulasEntrenadores.addAll(nuevasCedulas);
                adapterCedulasEntrenadores.notifyDataSetChanged();
                if (!edtCedulaEntrenador.isPopupShowing()) {
                    edtCedulaEntrenador.showDropDown();
                }
                edtNombreEntrenador.setText("");
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
        edtCodigoServicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Integer> nuevasCedulas = buscarCodigos(edtCedula.getText().toString());
                adapterCodigos.clear();
                adapterCodigos.addAll(nuevasCedulas);
                adapterCodigos.notifyDataSetChanged();
                if (!edtCodigoServicio.isPopupShowing()) {
                    edtCodigoServicio.showDropDown();
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        edtCedula.setOnItemClickListener((parent, view, position, id) -> {
            String seleccionado = adapterCedulas.getItem(position);

            String nombre = cargarNombre(seleccionado, "clientes");
            edtNombreCliente.setText(nombre);
        });
        edtCedulaEntrenador.setOnItemClickListener((parent, view, position, id) -> {
            String seleccionado = adapterCedulasEntrenadores.getItem(position);

            String nombre = cargarNombre(seleccionado, "entrenadores");
            edtNombreEntrenador.setText(nombre);
        });
        edtCodigoServicio.setOnItemClickListener((parent, view, position, id) -> {
            int seleccionado = adapterCodigos.getItem(position);
            Servicio servicio = servicioDB.buscarPorCodigo(seleccionado);
            edtNombreServicio.setText(servicio.getNombre());
            edtPrecioServicio.setText(String.valueOf(servicio.getPrecio()));
            editar = true;
            enEdicionServicio = servicio;
        });
    }

    private List<String> obtenerCedulas(String tabla) {
        List<String> lista = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cedula FROM " + tabla, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    private List<Integer> obtenerCodigos() {
        List<Integer> lista = new ArrayList<>();
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT codigo FROM servicios", null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public List<String> buscarCedulaPor(String criterio, String tabla){
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cedula FROM " + tabla + " WHERE cedula LIKE ?", new String[]{"%" + criterio + "%"});
        List<String> lista = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    public List<Integer> buscarCodigos(String criterio){
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT codigo FROM servicios WHERE codigo LIKE ?", new String[]{"%" + criterio + "%"});
        List<Integer> lista = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    private String cargarNombre(String cedula, String tabla){
        String valor = "";
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre FROM " + tabla + " WHERE cedula = '" + cedula + "'", null);
        if (cursor.moveToFirst()) {
            valor = cursor.getString(0);
        }
        cursor.close();
        return valor;
    }

    private void setItemSeleccionado(int position) {
        calcularPrecioTotal();
        itemseleccionado = position;
        if (position == -1){
            deshabilitarBotones();
            if (listaView == null || listaView.getChildCount() == 0)
                return;
            for (int i = 0; i < listaView.getChildCount(); i++)
                listaView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        } else {
            btnEditarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2196F3")));
            btnEliminarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D33232")));
        }
    }

    public void deshabilitarBotones() {
        btnEliminarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
        btnEditarServicio.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
    }

    private void cargarMembresia(int codigoMembresia, String cedulaCliente, String cedulaEntrenador, String nombreCliente, String nombreEntrenador, TipoMembresia tipo) {
        adaptador.setLista(servicioDB.buscarPorMembresia(codigoMembresia));
        adaptador.notifyDataSetChanged();

        edtCedula.setText(cedulaCliente);
        edtCedulaEntrenador.setText(cedulaEntrenador);
        edtNombreCliente.setText(nombreCliente);
        edtNombreEntrenador.setText(nombreEntrenador);

        spTipo.setSelection(tipo.ordinal());
    }

    private void calcularPrecioTotal(){
        double precio = 0.0;
        if (lista == null) return;
        precio = lista.getPrecioTotal();
        edtPrecioTotal.setText(String.valueOf(precio));
    }

}