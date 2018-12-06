package com.example.juan.concesionario;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PresupuestoAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<ExtrasPresupuesto> extras;

    private ArrayList<Double> sumatorioExtras;

    public PresupuestoAdapter (Activity activity, ArrayList<ExtrasPresupuesto>extras)
    {
        this.activity = activity;
        this.extras = extras;
    }

    private static class ViewHolder {
        private CheckBox checkbox;
        private TextView txvNombre, txvPrecio;
    }

    public ArrayList<ExtrasPresupuesto> getExtrasArray()
    {
        return this.extras;
    }

    @Override
    public int getCount() {
        return extras.size();
    }

    @Override
    public Object getItem(int position) {
        return extras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inf = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.layout_listview_presupuesto,null);
            holder = new ViewHolder();

            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.txvNombre = (TextView)convertView.findViewById(R.id.txvNombre);
            holder.txvPrecio = (TextView)convertView.findViewById(R.id.txvPrecio);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        if(extras.get(position).isCheckbox())
        {
            holder.checkbox.setChecked(true);
            //precioFinal = precioVehiculo + extras.get(position).getExtra().getPrecio();
            /*if(precioFinal < precioVehiculo)
            {
                precioFinal = precioVehiculo;
            }*/
        }
        else
        {
            holder.checkbox.setChecked(false);
            /*precioFinal = precioVehiculo - extras.get(position).getExtra().getPrecio();
            if(precioFinal < precioVehiculo)
            {
                precioFinal = precioVehiculo;
            }*/
        }
        holder.txvNombre.setText(extras.get(position).getExtra().getNombre());
        holder.txvPrecio.setText(String.valueOf(extras.get(position).getExtra().getPrecio()));

        return convertView;
    }

    public void setCheckBox(int position){
        ExtrasPresupuesto items = extras.get(position);
        items.setCheckbox(!items.isCheckbox());
        notifyDataSetChanged();
    }
}
