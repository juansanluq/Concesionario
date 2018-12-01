package com.example.juan.concesionario;

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

}
