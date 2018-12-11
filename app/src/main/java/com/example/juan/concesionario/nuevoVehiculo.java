package com.example.juan.concesionario;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class nuevoVehiculo extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgv;
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
        edtMarca = findViewById(R.id.edtMarca);
        edtModelo = findViewById(R.id.edtModelo);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        fab = findViewById(R.id.fab);
        switchNuevo = findViewById(R.id.switchNuevo);
        lv = findViewById(R.id.lv);
        imgv.setOnClickListener(this);
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
        if(v.getId() == findViewById(R.id.imgv).getId())
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
                                if (checkPermission()) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, RESULT_LOAD_IMG);
                                }
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
            Uri uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imgv.setImageBitmap(photo);
                nuevoVehiculo.setImagenBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPermission() {
        /* Se compureba de que la SDK es superior a marshmallow, pues si es inferior no es necesario
         * pedir permisos */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                /* En caso de no haber cargado correctamente los permisos se avisa con
                 * un Toast y se piden */
                Toast.makeText(getApplicationContext(), "Error al cargar permisos", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            } else {
                /* En caso de todos los permisos correctos se notifica */
                Toast.makeText(getApplicationContext(), "Todos los permisos se han cargado correctamente", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return true;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}