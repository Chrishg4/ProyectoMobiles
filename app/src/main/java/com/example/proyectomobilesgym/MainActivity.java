package com.example.proyectomobilesgym;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CustomAdapterMenu adaptador;
    private ListView listaView;
    private List<Class_Menu> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaView = findViewById(R.id.listMainMenu);
        inicializarLista();
        adaptador = new CustomAdapterMenu(this,lista);
        listaView.setAdapter(adaptador);

        listaView.setOnItemClickListener(
            (parent, view, position, id) -> {
                Class_Menu elemento = lista.get(position);
                Intent intent;
                switch (elemento.getId()) {
                    case 1:
                        intent = new Intent(this, Activity_Entrenadores.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(this, Activity_Clientes.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(this, Activity_Membresias.class);
                        startActivity(intent);
                        break;
                }
            }
        );


    }

    private void inicializarLista() {
        lista = new ArrayList<>();
        lista.add(new Class_Menu(1, R.drawable.entrenador, getString(R.string.title_trainers)));
        lista.add(new Class_Menu(2, R.drawable.client, getString(R.string.title_clients)));
        lista.add(new Class_Menu(3, R.drawable.membresia, getString(R.string.title_memberships)));
    }


}