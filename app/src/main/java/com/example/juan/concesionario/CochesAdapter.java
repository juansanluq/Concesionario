package com.example.juan.concesionario;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CochesAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<Vehiculo> vehiculos;

    public CochesAdapter (Activity activity, ArrayList<Vehiculo>vehiculos)
    {
        super(activity,R.layout.layout_listview_coches,vehiculos);
        this.activity = activity;
        this.vehiculos = vehiculos;
    }

    private static class ViewHolder {
        private ImageView imgv;
        private TextView txvMarca, txvModelo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_listview_coches,null);
            holder = new ViewHolder();

            holder.imgv = (ImageView) convertView.findViewById(R.id.imgv);
            holder.txvMarca = (TextView)convertView.findViewById(R.id.txvMarca);
            holder.txvModelo = (TextView)convertView.findViewById(R.id.txvModelo);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
            holder.imgv.setImageBitmap(vehiculos.get(position).getImagenBitmap());
            holder.txvMarca.setText(vehiculos.get(position).getMarca());
            holder.txvModelo.setText(vehiculos.get(position).getModelo());

        return convertView;
    }


}
