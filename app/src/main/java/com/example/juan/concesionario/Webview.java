package com.example.juan.concesionario;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import static com.example.juan.concesionario.DialogoDatos.hacer_presupuestoHTML;

public class Webview extends AppCompatActivity {
    private WebView wb;
    private FloatingActionButton fab, fabCancelar;
    File filehtml;
    File carpeta;
    File ficheroPDF;
    String path = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        filehtml = new File(path,"presupuesto.html");
        path = Environment.getExternalStorageDirectory().toString();
        wb = findViewById(R.id.wb);
        fab = findViewById(R.id.fab);
        fabCancelar = findViewById(R.id.fabCancelar);
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    FileOutputStream out = new FileOutputStream(filehtml);
                    byte[] s = hacer_presupuestoHTML().getBytes();
                    out.write(s);
                    out.close();
                    createPDF("presupuesto.pdf");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.add(Uri.fromFile(ficheroPDF));

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Presupuesto.extrasSeleccionados.clear();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    private void createPDF (String pdfFilename){

        //path for the PDF file to be generated
        carpeta = new File(Environment.getExternalStorageDirectory().toString(),"PDF");
        if(!carpeta.exists())
        {
            carpeta.mkdir();
        }
        ficheroPDF = new File (carpeta,pdfFilename);
        PdfWriter pdfWriter = null;

        //create a new document
        Document document = new Document();

        try {

            //get Instance of the PDFWriter
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(ficheroPDF));

            //document header attributes
            document.addAuthor("betterThanZero");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("MySampleCode.com");
            document.addTitle("Demo for iText XMLWorker");
            document.setPageSize(PageSize.LETTER);

            //open document
            document.open();

            //To convert a HTML file from the filesystem
            //String File_To_Convert = "docs/SamplePDF.html";
            //FileInputStream fis = new FileInputStream(File_To_Convert);

            //URL for HTML page
            InputStream is = new FileInputStream(filehtml);
            InputStreamReader fis = new InputStreamReader(is);

            //get the XMLWorkerHelper Instance

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            //convert to PDF
            worker.parseXHtml(pdfWriter, document, fis);

            //close the document
            document.close();
            //close the writer
            pdfWriter.close();

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
