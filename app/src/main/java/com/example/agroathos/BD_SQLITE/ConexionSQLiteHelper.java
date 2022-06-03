package com.example.agroathos.BD_SQLITE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_NIVEL1);
        db.execSQL(Utilidades.CREAR_TABLA_NIVEL1_5);
        db.execSQL(Utilidades.CREAR_TABLA_NIVEL2);
        db.execSQL(Utilidades.CREAR_DESTAJO_TABLA_NIVEL1);
        db.execSQL(Utilidades.CREAR_GARITA_TABLA_NIVEL1);
        db.execSQL(Utilidades.CREAR_GARITA_TABLA_NIVEL1_5);
        db.execSQL(Utilidades.CREAR_GARITA_TABLA_NIVEL2);
        db.execSQL(Utilidades.CREAR_GARITA_TABLA_NIVEL3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RRHH_NIVEL1");
        db.execSQL("DROP TABLE IF EXISTS RRHH_NIVEL1_5");
        db.execSQL("DROP TABLE IF EXISTS RRHH_NIVEL2");
        db.execSQL("DROP TABLE IF EXISTS RRHH_DESTAJO");
        db.execSQL("DROP TABLE IF EXISTS TRANSPORTE_GARITA_BUS");
        db.execSQL("DROP TABLE IF EXISTS TRANSPORTE_GARITA_BUS_PLACA");
        db.execSQL("DROP TABLE IF EXISTS TRANSPORTE_GARITA_PERSONAL");
        db.execSQL("DROP TABLE IF EXISTS TRANSPORTE_GARITA_UNIDAD");
        onCreate(db);
    }
}
