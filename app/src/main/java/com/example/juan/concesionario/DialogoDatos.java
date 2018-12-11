package com.example.juan.concesionario;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DialogoDatos extends DialogFragment {
    public static EditText edtNombre,edtTelefono,edtEmail, edtDireccion,edtPoblacion,edtFecha;

    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_presupuesto, null);
        builder.setView(view);
        // Add action buttons
        edtFecha = view.findViewById(R.id.edtFecha);
        edtNombre = view.findViewById(R.id.edtNombre);
        edtTelefono = view.findViewById(R.id.edtTelefono);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtDireccion = view.findViewById(R.id.edtDireccion);
        edtPoblacion = view.findViewById(R.id.edtPoblacion);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(checkPermission())
                        {
                                Intent i = new Intent(getContext(),Webview.class);
                                startActivity(i);

                                /*ArrayList<Uri> uris = new ArrayList<Uri>();
                                uris.add(Uri.fromFile(filehtml));
                                uris.add(Uri.fromFile(file));

                                String[] destinatario = new String[]{edtEmail.getText().toString()};

                                Intent enviar_email = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                enviar_email.putExtra(Intent.EXTRA_EMAIL,destinatario);
                                enviar_email.putExtra(Intent.EXTRA_SUBJECT,"Presupuesto Cliente " + edtNombre.getText().toString() + " | " + Principal.vehiculoDetalle.getMarca() + " " + Principal.vehiculoDetalle.getModelo());
                                enviar_email.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
                                enviar_email.putExtra(Intent.EXTRA_TEXT, "Gracias por visitar nuestro concesionario");
                                enviar_email.setType("text/html");
                                enviar_email.setType("image/jpg");
                                startActivityForResult(Intent.createChooser(enviar_email,"Elige la aplicación de mensajería"),1);*/
                        }
                    }
                })
                .setTitle("DATOS CLIENTE")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogoDatos.this.getDialog().cancel();
                    }
                });
        edtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }

        });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {

        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                edtFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    private boolean checkPermission() {
        /* Se compureba de que la SDK es superior a marshmallow, pues si es inferior no es necesario
         * pedir permisos */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(getActivity(),CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(getActivity(),WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(getActivity(),READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                /* En caso de no haber cargado correctamente los permisos se avisa con
                 * un Toast y se piden */
                Toast.makeText(getContext(), "Error al cargar permisos", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            } else {
                /* En caso de todos los permisos correctos se notifica */
                Toast.makeText(getContext(), "Todos los permisos se han cargado correctamente", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return true;
    }

    public static String hacer_presupuestoHTML ()
    {
        String html1 = "<!DOCTYPE html>\r\n" +
                "<html lang=\"es\">\r\n" +
                "<head>\r\n" +
                "<title>Titulo de la web| Menos de 55 caracteres</title>\r\n" +
                "<style>\r\n" +
                "table.minimalistBlack {\r\n" +
                "  border: 3px solid #000000;\r\n" +
                "  width: 100%;\r\n" +
                "  text-align: left;\r\n" +
                "  border-collapse: collapse;\r\n" +
                "}\r\n" +
                "table.minimalistBlack td, table.minimalistBlack th {\r\n" +
                "  border: 1px solid #000000;\r\n" +
                "  padding: 5px 4px;\r\n" +
                "}\r\n" +
                "table.minimalistBlack tbody td {\r\n" +
                "  font-size: 13px;\r\n" +
                "}\r\n" +
                "table.minimalistBlack th {\r\n" +
                "  text-align:center;\r\n" +
                "}\r\n" +
                "table.minimalistBlack thead {\r\n" +
                "  background: #CFCFCF;\r\n" +
                "  background: -moz-linear-gradient(top, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);\r\n" +
                "  background: -webkit-linear-gradient(top, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);\r\n" +
                "  background: linear-gradient(to bottom, #dbdbdb 0%, #d3d3d3 66%, #CFCFCF 100%);\r\n" +
                "  border-bottom: 3px solid #000000;\r\n" +
                "}\r\n" +
                "table.minimalistBlack thead th {\r\n" +
                "  font-size: 15px;\r\n" +
                "  font-weight: bold;\r\n" +
                "  color: #000000;\r\n" +
                "  text-align: center;\r\n" +
                "}\r\n" +
                "table.minimalistBlack tfoot {\r\n" +
                "  font-size: 14px;\r\n" +
                "  font-weight: bold;\r\n" +
                "  color: #000000;\r\n" +
                "  border-top: 3px solid #000000;\r\n" +
                "}\r\n" +
                "table.minimalistBlack tfoot td {\r\n" +
                "  font-size: 14px;\r\n" +
                "}\r\n" +
                "td.extra{\r\n" +
                "  width:80%;\r\n" +
                "}\r\n" +
                "td.modelo{\r\n" +
                "  font-size: 12px;\r\n" +
                "  font-weight: bold;\r\n" +
                "  color: #000000;\r\n" +
                "  text-align: center;\r\n" +
                "}\r\n" +
                "td.total{\r\n" +
                "  text-align: center;\r\n" +
                "}\r\n" +
                "table \r\n" +
                "	{\r\n" +
                "		border: 2px solid black;\r\n" +
                "		margin-top: 20px;\r\n" +
                "		width:100%;\r\n" +
                "	}\r\n" +
                "	\r\n" +
                "	td \r\n" +
                "	{\r\n" +
                "		padding: 5px;\r\n" +
                "	}\r\n" +
                "	\r\n" +
                "	th \r\n" +
                "	{\r\n" +
                "		border-bottom: 1px solid black;\r\n" +
                "	}\r\n" +
                "</style>\r\n" +
                "</head>\r\n" +
                "<body>\r\n" +
                "<h1 align = \"center\">Presupuesto</h1>\r\n" +
                "  <table>\r\n" +
                "	<tr>\r\n" +
                "		<td colspan=\"2\"> Concesionario Mis Cojones </td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> NIF: 25158415T </td>\r\n" +
                "		<td align=\"right\" rowspan=\"6\"> <img src=\"https://images0.autocasion.com/unsafe/origxorig/adv/04/2895/c2149a929118570d5898b1ceb7e70d931e668dce.jpeg\" height=\"100px\" width=\"200px\"> </img> </td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Dirección: C/ Alberto Aguilera, 1 </td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> 28015 Madrid </td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Teléfonos: 683 251 205 - 914 301 500 </td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Correo electrónico: A15motor@gmail.com  </td>\r\n" +
                "	</tr>\r\n" +
                "  </table>\r\n" +
                "  \r\n" +
                "  <table width=\"100%\">\r\n" +
                "	<tr>\r\n" +
                "		<th> Cliente: </th>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Nombre: ";
        String nombre_cliente = edtNombre.getText().toString();
        String html1_1 = "</td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Teléfono: ";
        String telefono = edtTelefono.getText().toString();
        String html1_2 = "</td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Email: ";
        String email = edtEmail.getText().toString();
        String html1_3 = "</td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Dirección: ";
        String direccion = edtDireccion.getText().toString();
        String html1_4 = "</td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Población: ";
        String poblacion = edtPoblacion.getText().toString();
        String html1_5 = "</td>\r\n" +
                "	</tr>\r\n" +
                "	<tr>\r\n" +
                "		<td> Fecha Nacimiento: ";
        String fecha_nacimiento = edtFecha.getText().toString();
        String html1_6 = "</td>\r\n" +
                "	</tr>\r\n" +
                "  </table>\r\n" +
                "<table class=\"minimalistBlack\">\r\n" +
                "<thead>\r\n" +
                "<tr>\r\n" +
                "<th colspan=\"5\">";
        String marca_coche = Principal.vehiculoDetalle.getMarca().toUpperCase();
        String modelo_coche = Principal.vehiculoDetalle.getModelo().toUpperCase();
        String precio_base = "Precio base del vehículo";
        String precio_base_value = String.valueOf(Principal.vehiculoDetalle.getPrecio()) + " €";
        String html2 = "</th>\r\n" +
                "				</tr>\r\n" +
                "			</thead>\r\n" +
                "			<tfoot>\r\n" +
                "				<tr>\r\n" +
                "				<td class=\"total\" colspan=\"4\">TOTAL:</td>\r\n" +
                "				<td>";
        String precio_total = String.valueOf(Presupuesto.sumatorio) + " €";

        String html3 = "</td>\r\n" +
                "				</tr>\r\n" +
                "			</tfoot>\r\n" +
                "			<tbody>\r\n" +
                "				<tr>\r\n" +
                "					<td class=\"modelo\" colspan=\"5\">";
        String final_html = "</tbody>\r\n" +
                "</table>\r\n" +
                "</body>\r\n" +
                "</html>";

        //String resultado = html1 + marca_coche + html2 + precio_total + html3 + modelo_coche;
        String resultado = html1 + nombre_cliente + html1_1 + telefono + html1_2 + email + html1_3 + direccion + html1_4 + poblacion + html1_5 + fecha_nacimiento + html1_6 + marca_coche + html2 + precio_total + html3 + modelo_coche;
        int size = Presupuesto.extrasSeleccionados.size();
        for (int i = 0; i < Presupuesto.extrasSeleccionados.size(); i++)
        {
            String fila_p1 = "</tr>\r\n" +
                    "				<tr>\r\n" +
                    "					<td class=\"extra\" colspan=\"4\">";
            String fila_nombre_extra = Presupuesto.extrasSeleccionados.get(i).getNombre();
            String fila_p2 = "</td>\r\n" +
                    "					<td>";
            String fila_precio_extra = String.valueOf(Presupuesto.extrasSeleccionados.get(i).getPrecio()) + " €";
            String fila_p3 = "</td>\r\n" +
                    "				</tr>\r\n";
            if (i == 0)
            {
                resultado = resultado + fila_p1 + precio_base + fila_p2 + precio_base_value + fila_p3;
            }
            String fila = fila_p1 + fila_nombre_extra + fila_p2 + fila_precio_extra + fila_p3;
            resultado = resultado + fila;
        }

        resultado = resultado + final_html;

        return resultado;
    }
}
