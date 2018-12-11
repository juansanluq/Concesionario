package com.example.juan.concesionario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.juan.concesionario.DialogoDatos.hacer_presupuestoHTML;

public class Webview extends AppCompatActivity {
    private WebView wb;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wb = findViewById(R.id.wb);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut = null;
                File file = new File(path,"coche.jpg");
                File filehtml = new File(path,"presupuesto.html");

                try {
                    fOut = new FileOutputStream(file);
                    Principal.vehiculoDetalle.getImagenBitmap().compress(Bitmap.CompressFormat.JPEG,85,fOut);
                    fOut.flush();
                    fOut.close();
                    MediaStore.Images.Media.insertImage(Presupuesto.contextOfApplication.getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

                    FileOutputStream out = new FileOutputStream(filehtml);
                    out.write(DialogoDatos.hacer_presupuestoHTML().getBytes());
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(Uri.fromFile(filehtml));
                uris.add(Uri.fromFile(file));

                String[] destinatario = new String[]{DialogoDatos.edtEmail.getText().toString()};

                Intent enviar_email = new Intent(Intent.ACTION_SEND_MULTIPLE);
                enviar_email.putExtra(Intent.EXTRA_EMAIL,destinatario);
                enviar_email.putExtra(Intent.EXTRA_SUBJECT,"Presupuesto Cliente " + DialogoDatos.edtNombre.getText().toString() + " | " + Principal.vehiculoDetalle.getMarca() + " " + Principal.vehiculoDetalle.getModelo());
                enviar_email.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
                enviar_email.putExtra(Intent.EXTRA_TEXT, "Gracias por visitar nuestro concesionario");
                enviar_email.setType("text/html");
                enviar_email.setType("image/jpg");
                startActivityForResult(Intent.createChooser(enviar_email,"Elige la aplicación de mensajería"),1);
            }
        });
        WebSettings settings = wb.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        wb.loadData(hacer_presupuestoHTML(),"text/html; charset=utf-8", "utf-8");
    }
}
