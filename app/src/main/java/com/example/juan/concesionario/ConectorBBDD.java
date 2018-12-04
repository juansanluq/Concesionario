package com.example.juan.concesionario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class ConectorBBDD extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "concesionario.db";
    private static final int DATABASE_VERSION = 1;

    public ConectorBBDD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Vehiculo recuperarVehiculo(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Vehiculo vehiculo = new Vehiculo();
        String[] valores_recuperar = {"id","marca","modelo","imagen","precio","descripcion","nuevo"};
        Cursor c = db.query("vehiculos",valores_recuperar,"id = "+ id,null,null,null,null,null);
        if(c.getCount() > 0)
        {
            c.moveToFirst();
            vehiculo = new Vehiculo(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getDouble(4),c.getString(5),c.getInt(6));
            c.close();
            db.close();
            return vehiculo;
        }
        else
        {
            c.close();
            db.close();
            return vehiculo;
        }
    }


    public ArrayList<Vehiculo> recuperarVehiculosNuevos()
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Vehiculo> lista_vehiculos = new ArrayList<Vehiculo>();
        String[] valores_recuperar = {"id","marca","modelo","imagen","precio","descripcion","nuevo"};
        Cursor c = db.query("vehiculos",valores_recuperar,"nuevo = 1",null,null,null,null,null);
        if(c.getCount() > 0)
        {
            c.moveToFirst();
            do {
                Vehiculo vehiculo = new Vehiculo(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getDouble(4),c.getString(5),c.getInt(6));
                lista_vehiculos.add(vehiculo);
            }while(c.moveToNext());
            db.close();
            c.close();
            return lista_vehiculos;
        }
        else {
            c.close();
            db.close();
            return lista_vehiculos;
        }
    }

    public ArrayList<Vehiculo> recuperarVehiculosOcasion()
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Vehiculo> lista_vehiculos = new ArrayList<Vehiculo>();
        String[] valores_recuperar = {"id","marca","modelo","imagen","precio","descripcion","nuevo"};
        Cursor c = db.query("vehiculos",valores_recuperar,"nuevo = 0",null,null,null,null,null);
        if(c.getCount() > 0)
        {
            c.moveToFirst();
            do {
                Vehiculo vehiculo = new Vehiculo(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getDouble(4),c.getString(5),c.getInt(6));
                lista_vehiculos.add(vehiculo);
            }while(c.moveToNext());
            db.close();
            c.close();
            return lista_vehiculos;
        }
        else {
            c.close();
            db.close();
            return lista_vehiculos;
        }
    }

    public void modificarVehiculo(Vehiculo vehiculo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("marca", vehiculo.getMarca());
        valores.put("modelo", vehiculo.getModelo());
        valores.put("precio", vehiculo.getPrecio());
        valores.put("descripcion", vehiculo.getDescripcion());
        valores.put("nuevo", vehiculo.isNuevo());
        db.update("vehiculos", valores, "id = " + String.valueOf(vehiculo.getId()), null);
        db.close();
    }

    public void borrarVehiculo(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("vehiculos", "id="+id, null);
        db.close();
    }

    public Extra recuperarExtra(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Extra extra = new Extra();
        String[] valores_recuperar = {"id","nombre","descripcion","precio"};
        Cursor c = db.query("extras",valores_recuperar,"id = "+ id,null,null,null,null,null);
        if(c.getCount() > 0)
        {
            c.moveToFirst();
            extra = new Extra(c.getInt(0),c.getString(1),c.getString(2),c.getDouble(3));
            c.close();
            db.close();
            return extra;
        }
        else
        {
            c.close();
            db.close();
            return extra;
        }
    }

    public ArrayList<Extra> recuperarExtras()
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Extra> lista_extras = new ArrayList<Extra>();
        String[] valores_recuperar = {"id","nombre","descripcion","precio"};
        Cursor c = db.query("extras",valores_recuperar,null,null,null,null,null,null);
        if(c.getCount() > 0)
        {
            c.moveToFirst();
            do {
                Extra extra = new Extra(c.getInt(0),c.getString(1),c.getString(2),c.getDouble(3));
                lista_extras.add(extra);
            }while(c.moveToNext());
            db.close();
            c.close();
            return lista_extras;
        }
        else {
            c.close();
            db.close();
            return lista_extras;
        }
    }

    public void borrarExtra(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("extras", "id="+id, null);
        db.close();
    }

}
