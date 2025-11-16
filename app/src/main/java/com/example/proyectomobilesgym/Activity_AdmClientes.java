package com.example.proyectomobilesgym;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.Arrays;

public class Activity_AdmClientes extends AppCompatActivity {

    EditText etCedula, etNombre, etNumero, etEdad, EtPeso, etAltura;

    Spinner spGenero;

    Button btnGuadar, btnCancelar;

    ImageView imgClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adm_clientes);

        etCedula = findViewById(R.id.edtCedula);
        etNombre = findViewById(R.id.edtNombre);
        etNumero = findViewById(R.id.edtNumero);
        etEdad = findViewById(R.id.edtEdad);
        EtPeso = findViewById(R.id.edtPeso);
        etAltura = findViewById(R.id.edtAltura);
        imgClientes = findViewById(R.id.imgClientes);

        spGenero = findViewById(R.id.spGenero);

        btnGuadar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        //obtener los generos del enum como un array de strings con un for
        Genero[] generosEnum = Genero.values();//devuelve un array con los valores del enum
        String[] generos = new String[generosEnum.length];//crea un array de strings con la misma longitud que el enum
        for (int i = 0; i < generosEnum.length; i++) {
            generos[i] = generosEnum[i].name();
        }

        //configurar el spinner con los generos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapter);

        //poner la imagen segun el genero seleccionado
        spGenero.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String generoSeleccionado = spGenero.getSelectedItem().toString();
                if (generoSeleccionado.equalsIgnoreCase("HOMBRE")) {
                    imgClientes.setImageResource(R.drawable.user_h);
                } else if (generoSeleccionado.equalsIgnoreCase("MUJER")) {
                    imgClientes.setImageResource(R.drawable.user_m);
                } else {
                    imgClientes.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                imgClientes.setImageResource(R.drawable.ic_launcher_foreground);
            }
        });
        // Obtengo los datos enviados desde el main de clientes anterior
        String id = getIntent().getStringExtra("cedula");
        if (id != null) {
            // valido los datos de id no sean nulos para cargar la info
            etCedula.setText(getIntent().getStringExtra("cedula"));
            etNombre.setText(getIntent().getStringExtra("nombre"));
            etNumero.setText(getIntent().getStringExtra("numero"));

            String generoCliente = getIntent().getStringExtra("genero");
            int spinnerPosition = adapter.getPosition(generoCliente);
            spGenero.setSelection(spinnerPosition);

            // convierto los int y double a string para mostrarlos en los editText
            etEdad.setText(String.valueOf(getIntent().getIntExtra("edad", 0)));
            etAltura.setText(String.valueOf(getIntent().getDoubleExtra("altura", 0.0)));
            EtPeso.setText(String.valueOf(getIntent().getDoubleExtra("peso", 0.0)));
            // hago que el texto cambie a editar con las variables que hay en values
            btnGuadar.setText(getString(R.string.btn_edit));
            etCedula.setEnabled(false);
        }else {
            Toast.makeText(this, "No se pudieron jalar los datos", Toast.LENGTH_SHORT).show();
        }


    }

    public void BtnCancelar(View view) {
        finish();
    }

    public void BtnGuardar(View view) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getWritableDatabase();
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            // Editar cliente existente
            ContentValues registro = new android.content.ContentValues();
            registro.put("nombre", etNombre.getText().toString());
            registro.put("numero", Integer.parseInt(etNumero.getText().toString()));
            registro.put("genero", spGenero.getSelectedItem().toString());
            registro.put("edad", Integer.parseInt(etEdad.getText().toString()));
            registro.put("altura", Double.parseDouble(etAltura.getText().toString()));
            registro.put("peso", Double.parseDouble(EtPeso.getText().toString()));

            int filasAfectadas = db.update("clientes", registro, "cedula=?", new String[]{id});
            db.close();
            if (filasAfectadas > 0) {
                Toast.makeText(this, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Agregar un nuevo cliente
            if (validarCedula(etCedula.getText())) {
                Toast.makeText(this, "La cédula ya existe", Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues registro = new ContentValues();
            registro.put("cedula", etCedula.getText().toString());
            registro.put("nombre", etNombre.getText().toString());
            registro.put("numero", Integer.parseInt(etNumero.getText().toString()));
            registro.put("genero", spGenero.getSelectedItem().toString());
            registro.put("edad", Integer.parseInt(etEdad.getText().toString()));
            registro.put("altura", Double.parseDouble(etAltura.getText().toString()));
            registro.put("peso", Double.parseDouble(EtPeso.getText().toString()));
            db.insert("clientes", null, registro);
            db.close();
            Toast.makeText(this, "Cliente registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //valida la cadula
    public boolean validarCedula(Editable cedula) {
        AdminDB admin = new AdminDB(this);
        SQLiteDatabase db = admin.getReadableDatabase();
        String[] ced = new String[]{cedula.toString()};
        Cursor cursor = db.rawQuery("SELECT cedula FROM clientes WHERE cedula=?", ced);
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }

}