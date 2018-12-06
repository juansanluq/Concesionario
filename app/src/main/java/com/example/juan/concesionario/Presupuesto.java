package com.example.juan.concesionario;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Presupuesto extends AppCompatActivity implements View.OnClickListener {

    private TextView txvMarcaModelo, txvPrecioToolbar;
    private Toolbar toolbar;
    private ListView lv;
    private Button btnCalcular;
    private PresupuestoAdapter adaptador;
    double precioVehiculo = Principal.vehiculoDetalle.getPrecio();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(this);
        txvMarcaModelo = findViewById(R.id.txvMarcaModelo);
        txvPrecioToolbar = findViewById(R.id.txvPrecio);
        txvPrecioToolbar.setText(String.valueOf(precioVehiculo) + " €");
        txvMarcaModelo.setText(Principal.vehiculoDetalle.getMarca() + " " + Principal.vehiculoDetalle.getModelo());
        lv = findViewById(R.id.lv);

        final ArrayList<ExtrasPresupuesto> extrasPresupuesto = new ArrayList<ExtrasPresupuesto>();

        for (int i = 0; i < Principal.lista_extras.size(); i++)
        {
            extrasPresupuesto.add(new ExtrasPresupuesto(Principal.lista_extras.get(i)));
        }

        adaptador = new PresupuestoAdapter(this,extrasPresupuesto);
        lv.setAdapter(adaptador);

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

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogoDatos dialog = new DialogoDatos();
                dialog.show(getSupportFragmentManager(),"Dialogo");

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(btnCalcular.findViewById(R.id.btnCalcular).getId() == v.getId())
        {
            double sumatorio = precioVehiculo;
            for(int i=0;i<adaptador.getExtrasArray().size();i++)
            {
                if(adaptador.getExtrasArray().get(i).isCheckbox())
                {
                    sumatorio = sumatorio + adaptador.getExtrasArray().get(i).getExtra().getPrecio();
                }
            }
            txvPrecioToolbar.setText(String.valueOf(sumatorio));
        }
    }
}
