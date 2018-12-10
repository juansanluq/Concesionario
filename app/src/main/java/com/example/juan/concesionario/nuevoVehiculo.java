package com.example.juan.concesionario;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class nuevoVehiculo extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgv;
    private Button btnImagen;
    private EditText edtMarca, edtModelo, edtDescripcion, edtPrecio;
    private Switch switchNuevo;
    private FloatingActionButton fab;
    private int CAMERA_REQUEST = 1;
    private int RESULT_LOAD_IMG = 2;
    private Vehiculo nuevoVehiculo;
    private Bitmap photo;
    private boolean btnImagenPulsado = false;
    private ListView lv;
    private PresupuestoAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_vehiculo);

        imgv = findViewById(R.id.imgv);
        btnImagen = findViewById(R.id.btnImagen);
        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        fab = findViewById(R.id.fab);
        switchNuevo = findViewById(R.id.switchNuevo);
        lv = findViewById(R.id.lv);
        btnImagen.setOnClickListener(this);
        nuevoVehiculo = new Vehiculo();

        final ArrayList<ExtrasPresupuesto> extrasPresupuesto = new ArrayList<ExtrasPresupuesto>();

        for (int i = 0; i < Principal.lista_extras.size(); i++)
        {
            extrasPresupuesto.add(new ExtrasPresupuesto(Principal.lista_extras.get(i)));
        }

        adaptador = new PresupuestoAdapter(this,extrasPresupuesto,true);
        lv.setAdapter(adaptador);
        lv.setVisibility(View.GONE);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                adaptador.setCheckBox(position);
            }
        });

        switchNuevo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    lv.setVisibility(View.GONE);
                }
                else
                {
                    lv.setVisibility(View.VISIBLE);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtMarca.getText().toString().isEmpty() || edtModelo.getText().toString().isEmpty() || edtDescripcion.getText().toString().isEmpty() || edtPrecio.getText().toString().isEmpty() || !btnImagenPulsado)
                {
                    Toast.makeText(getApplicationContext(),"ES OBLIGATORIO RELLENAR TODOS LOS DATOS ANTES DE INSERTAR EL VEHICULO",Toast.LENGTH_LONG).show();
                }
                else
                {
                    nuevoVehiculo.setMarca(edtMarca.getText().toString());
                    nuevoVehiculo.setModelo(edtModelo.getText().toString());
                    nuevoVehiculo.setDescripcion(edtModelo.getText().toString());
                    nuevoVehiculo.setPrecio(Integer.parseInt(edtPrecio.getText().toString()));
                    if(switchNuevo.isChecked())
                    {
                        nuevoVehiculo.setNuevo(true);
                    }
                    else
                    {
                        nuevoVehiculo.setNuevo(false);
                    }
                    Principal.baseDatos.insertarVehiculo(nuevoVehiculo);
                    ArrayList<Extra> extrasInsertar = new ArrayList<Extra>();
                    if(!nuevoVehiculo.isNuevo())
                    {
                        for(int i=0;i<adaptador.getExtrasArray().size();i++)
                        {
                            if(adaptador.getExtrasArray().get(i).isCheckbox())
                            {
                                extrasInsertar.add(adaptador.getExtrasArray().get(i).getExtra());
                            }
                        }
                    }
                    int ID_vehiculo = Principal.baseDatos.getHighestID("vehiculos");
                    Principal.baseDatos.insertarExtraVehiculo(ID_vehiculo,extrasInsertar);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.btnImagen).getId())
        {
            btnImagenPulsado = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona método de entrada")
                    .setItems(R.array.opcionesEntrada, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == 0)
                            {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                            if(which == 1)
                            {
                                Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                galeria.setType("image/*");
                                startActivityForResult(galeria, RESULT_LOAD_IMG);
                            }
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imgv.setImageBitmap(photo);
            nuevoVehiculo.setImagenBitmap(photo);
        }
        if(requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK)
        {
            Uri imageUri = data.getData();
            imgv.setImageURI(imageUri);
            //imgv.getDrawingCache();
            //Bitmap imagenGaleria = imgv.getDrawingCache();
            //nuevoVehiculo.setImagenBitmap(imagenGaleria);
        }
    }
}