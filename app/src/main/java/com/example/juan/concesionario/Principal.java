package com.example.juan.concesionario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Principal extends AppCompatActivity {
    private ImageView imgv;
    private Vehiculo vehiculoMostrar;
    private ConectorBBDD baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        imgv = findViewById(R.id.imgv);
        baseDatos = new ConectorBBDD(this);

        vehiculoMostrar = baseDatos.recuperarVehiculo(2);
        imgv.setImageBitmap(vehiculoMostrar.getImagenBitmap());


    }
}
