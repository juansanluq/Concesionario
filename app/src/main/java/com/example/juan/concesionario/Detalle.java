package com.example.juan.concesionario;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import javax.security.auth.PrivateCredentialPermission;

public class Detalle extends AppCompatActivity {
    private AppBarLayout app_bar;
    private EditText edtMarca, edtModelo, edtDescripcion, edtPrecio;
    private FloatingActionButton fab;
    private Animation animDesaparecer, animAparecer, animDesaparecerGuardar;
    private boolean presupuesto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar.setTitle(Principal.vehiculoDetalle.getMarca() + " " + Principal.vehiculoDetalle.getModelo());
        app_bar.setBackground(new BitmapDrawable(getResources(), Principal.vehiculoDetalle.getImagenBitmap()));
        setSupportActionBar(toolbar);

        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);

        edtMarca.setText(Principal.vehiculoDetalle.getMarca());
        edtModelo.setText(Principal.vehiculoDetalle.getModelo());
        edtDescripcion.setText(Principal.vehiculoDetalle.getDescripcion());
        edtPrecio.setText(String.valueOf(Principal.vehiculoDetalle.getPrecio()));

        animDesaparecer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.desaparecer);
        animAparecer = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.aparecer);
        animDesaparecerGuardar = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.desaparecer);


        animDesaparecer.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.verde)));
                fab.setImageResource(R.drawable.ic_save);
                fab.startAnimation(animAparecer);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animDesaparecerGuardar.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                fab.setImageResource(R.drawable.ic_presupuesto);
                fab.startAnimation(animAparecer);
                presupuesto = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        /*edtMarca.setFocusable(false);
        edtModelo.setFocusable(false);
        edtDescripcion.setFocusable(false);
        edtPrecio.setFocusable(false);*/



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presupuesto)
                {
                    Intent i = new Intent(getApplicationContext(),Presupuesto.class);
                    startActivity(i);
                }
                else
                {
                    fab.startAnimation(animDesaparecerGuardar);

                    Principal.vehiculoDetalle.setMarca(edtMarca.getText().toString());
                    Principal.vehiculoDetalle.setModelo(edtModelo.getText().toString());
                    Principal.vehiculoDetalle.setDescripcion(edtDescripcion.getText().toString());
                    Principal.vehiculoDetalle.setPrecio(Double.parseDouble(edtPrecio.getText().toString()));

                    Principal.baseDatos.modificarVehiculo(Principal.vehiculoDetalle);

                    edtMarca.setFocusable(false);
                    edtModelo.setFocusable(false);
                    edtDescripcion.setFocusable(false);
                    edtPrecio.setFocusable(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.itModificar:
                presupuesto = false;
                edtMarca.setFocusableInTouchMode(true);
                edtModelo.setFocusableInTouchMode(true);
                edtDescripcion.setFocusableInTouchMode(true);
                edtPrecio.setFocusableInTouchMode(true);
                edtMarca.requestFocus();
                fab.startAnimation(animDesaparecer);
                return true;
            case R.id.itEliminar:
                Principal.baseDatos.borrarVehiculo(Principal.vehiculoDetalle.getId());
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
