package com.example.juan.concesionario;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class ConectorBBDD extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "concesionario.db";
    private static final int DATABASE_VERSION = 2;

    public ConectorBBDD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Vehiculo recuperarVehiculo(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Vehiculo vehiculo = new Vehiculo();
        String[] valores_recuperar = {"id","marca","modelo","imagen","precio","descripcion"};
        Cursor c = db.query("vehiculos",valores_recuperar,"id = "+ id,null,null,null,null,null);
        if(c.getCount() > 0)
        {
            c.moveToFirst();
            vehiculo = new Vehiculo(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getDouble(4),c.getString(5));
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
}
