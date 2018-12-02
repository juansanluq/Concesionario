package com.example.juan.concesionario;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExtrasAdapter extends ArrayAdapter {

    private Activity activity;
    private ArrayList<Extra> extras;

    public ExtrasAdapter (Activity activity, ArrayList<Extra> extras)
    {
        super(activity,R.layout.layout_listview_extras,extras);
        this.activity = activity;
        this.extras = extras;
    }

    private static class ViewHolder
    {
        private TextView txvNombre, txvDescripcion, txvPrecio;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_listview_extras,null);
            holder = new ViewHolder();

            holder.txvNombre = (TextView) convertView.findViewById(R.id.txvNombre);
            holder.txvDescripcion = (TextView)convertView.findViewById(R.id.txvDescripcion);
            holder.txvPrecio = (TextView)convertView.findViewById(R.id.txvPrecio);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txvNombre.setText(extras.get(position).getNombre());
        holder.txvDescripcion.setText(extras.get(position).getDescripcion());
        int tamañoDescripcion = Math.round(holder.txvDescripcion.getTextSize());
        String descripcion = String.valueOf(holder.txvDescripcion.getText()).substring(0,tamañoDescripcion - 5) + "...";
        holder.txvDescripcion.setText(descripcion);
        holder.txvPrecio.setText(String.valueOf(extras.get(position).getPrecio()) + " €");

        return convertView;
    }

}
