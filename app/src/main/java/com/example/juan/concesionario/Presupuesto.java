package com.example.juan.concesionario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.List;

public class Presupuesto extends AppCompatActivity {

    private TextView txvMarcaModelo;
    private Toolbar toolbar;
    private ListView lv;
    private PresupuestoAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txvMarcaModelo = findViewById(R.id.txvMarcaModelo);
        txvMarcaModelo.setText(Principal.vehiculoDetalle.getMarca() + " " + Principal.vehiculoDetalle.getModelo());
        lv = findViewById(R.id.lv);
        adaptador = new PresupuestoAdapter(this,Principal.lista_extras);
        lv.setAdapter(adaptador);

    }
}
