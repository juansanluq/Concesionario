package com.example.juan.concesionario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class ConectorBBDD extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "concesionario.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db_escritura;
    private SQLiteDatabase db_lectura;

    public ConectorBBDD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db_escritura = getWritableDatabase();
        db_lectura = getReadableDatabase();
    }
}
