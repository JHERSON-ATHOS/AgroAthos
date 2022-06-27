package com.example.agroathos.TRANSPORTE_GARITA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_Fundos;
import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.ENTIDADES.E_Zonas;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorFundos;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorZonas;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;

import org.json.JSONObject;

import java.util.ArrayList;

public class PrimerNivelWelcomeGarita extends AppCompatActivity {

    Button btnBus, btnPersonal, btnUnidad;
    Spinner spZona, spFundo;

    ArrayList<E_Zonas> zonasArrayListPersonalizado = new ArrayList<>();
    ArrayList<E_Fundos> fundosArrayListPersonalizado = new ArrayList<>();
    AdaptadorZonas adaptadorZonasPersonalizado;
    AdaptadorFundos adaptadorFundosPersonalizado;

    ConexionSQLiteHelper conn;

    String zona = "";
    String fundo = "";
    Toolbar toolbar;

    //DATOS GARITA BUS
    ArrayList<String> arrayListIdBUS = new ArrayList<>();
    ArrayList<String> arrayListAnexoPlacaBUS = new ArrayList<>();
    ArrayList<String> arrayListZonaBUS = new ArrayList<>();
    ArrayList<String> arrayListFundoBUS = new ArrayList<>();
    ArrayList<String> arrayListPersonalBUS = new ArrayList<>();
    ArrayList<String> arrayListFechaBUS = new ArrayList<>();
    ArrayList<String> arrayListHoraBUS = new ArrayList<>();
    ArrayList<String> arrayListSincBUS = new ArrayList<>();

    //DATOS GARITA BUS INTERMEDIO
    ArrayList<String> arrayListIdBUSINTERMEDIO = new ArrayList<>();
    ArrayList<String> arrayListAnexoPersonalBUSINTERMEDIO = new ArrayList<>();
    ArrayList<String> arrayListContadorBUSINTERMEDIO = new ArrayList<>();
    ArrayList<String> arrayListPlacaBUSINTERMEDIO = new ArrayList<>();
    ArrayList<String> arrayListSincBUSINTERMEDIO = new ArrayList<>();

    //DATOS GARITA PERSONAL
    ArrayList<String> arrayListIdPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListZonaPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListFundoPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListPersonalPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListTipoHoraPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListFechaPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListHoraPERSONAL = new ArrayList<>();
    ArrayList<String> arrayListSincPERSONAL = new ArrayList<>();

    //DATOS GARITA UNIDAD
    ArrayList<String> arrayListIdUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListZonaUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListFundoUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListPersonalUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListTipoHoraUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListFechaUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListHoraUNIDAD = new ArrayList<>();
    ArrayList<String> arrayListSincUNIDAD = new ArrayList<>();

    int validarBUS = 0;
    int validarBUSINTERMEDIO = 0;
    int validarPERSONAL = 0;
    int validarUNIDAD = 0;

    //ACTUALIZACIÓN
    ArrayList<String> idBusNivelUno = new ArrayList<>();
    ArrayList<String> idBusNivelDos = new ArrayList<>();
    ArrayList<String> idPersonalNivelUno = new ArrayList<>();
    ArrayList<String> idUnidadNivelUno = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome_garita);

        toolbar = findViewById(R.id.toolbarPRIMER_NIVEL_LISTAR_REGISTROS_GARITA);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("¡JUNTOS HACEMOS MÁS!");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        conn = new ConexionSQLiteHelper(this,"athos0",null, Utilidades.VERSION_APP);

        btnBus = findViewById(R.id.btnRegistrarBusGARITA);
        btnPersonal = findViewById(R.id.btnRegistrarPersonalGARITA);
        btnUnidad = findViewById(R.id.btnRegistrarUnidadGARITA);
        spZona = findViewById(R.id.spZonaTRANSPORTE_GARITA);
        spFundo = findViewById(R.id.spFundoTRANSPORTE_GARITA);

        validarAccion();
        cargarZonas();

        btnBus.setOnClickListener(view ->
                {
                    if (zona.equals("-- Selecciona una Zona --")){
                        Toast.makeText(this, "Secciona una Zona", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (fundo.equals("-- Selecciona un Fundo --")){
                        Toast.makeText(this, "Secciona un Fundo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    enviarDatos(1, "");
                });
        btnPersonal.setOnClickListener(view -> {
            if (zona.equals("-- Selecciona una Zona --")){
                Toast.makeText(this, "Secciona una Zona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fundo.equals("-- Selecciona un Fundo --")){
                Toast.makeText(this, "Secciona un Fundo", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO DE HORAS");
            builder.setPositiveButton("INGRESO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(2, "INGRESO");
                }
            });
            builder.setNegativeButton("SALIDA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(2, "SALIDA");
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        btnUnidad.setOnClickListener(view -> {
            if (zona.equals("-- Selecciona una Zona --")){
                Toast.makeText(this, "Secciona una Zona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fundo.equals("-- Selecciona un Fundo --")){
                Toast.makeText(this, "Secciona un Fundo", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO DE HORAS");
            builder.setPositiveButton("INGRESO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(3, "INGRESO");
                }
            });
            builder.setNegativeButton("SALIDA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(3, "SALIDA");
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        spZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Zonas e_zonas = zonasArrayListPersonalizado.get(position);
                zona = e_zonas.getNombre();

                switch (zona){
                    case "ICA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos("-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos("PLANTA SANTIAGO"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "LDN"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "LPO"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "MAC"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "MEN"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "PAR"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SAT"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SNA"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SOJ"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "STF"));
                        break;
                    case "PISCO":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos("-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos("LIN"));
                        break;
                    case "NAZCA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "MAT"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "LUC"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SOI"));
                        break;
                    case "CARAZ":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "CHI"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "POM"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SCA"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SLA"));
                        break;
                    case "NEPEÑA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "CAY"));
                        break;
                }

                adaptadorFundosPersonalizado = new AdaptadorFundos(PrimerNivelWelcomeGarita.this, fundosArrayListPersonalizado);
                spFundo.setAdapter(adaptadorFundosPersonalizado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spFundo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Fundos e_fundos = fundosArrayListPersonalizado.get(position);
                fundo = e_fundos.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void cargarZonas(){
        zonasArrayListPersonalizado.add(new E_Zonas("-- Selecciona una Zona --"));
        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));

        zonasArrayListPersonalizado.add(new E_Zonas( "CARAZ"));
        zonasArrayListPersonalizado.add(new E_Zonas( "ICA"));
        zonasArrayListPersonalizado.add(new E_Zonas( "PISCO"));
        zonasArrayListPersonalizado.add(new E_Zonas( "NEPEÑA"));
        zonasArrayListPersonalizado.add(new E_Zonas( "NAZCA"));

        adaptadorZonasPersonalizado = new AdaptadorZonas(PrimerNivelWelcomeGarita.this, zonasArrayListPersonalizado);
        adaptadorFundosPersonalizado = new AdaptadorFundos(PrimerNivelWelcomeGarita.this, fundosArrayListPersonalizado);

        spZona.setAdapter(adaptadorZonasPersonalizado);
        spFundo.setAdapter(adaptadorFundosPersonalizado);
    }

    public void enviarDatos(int valor, String tipo){
        Intent intent = new Intent(PrimerNivelWelcomeGarita.this, SegundoNivelRegistrarPersonal.class);

        Bundle bundle = new Bundle();
        bundle.putString("zona", zona);
        bundle.putString("fundo", fundo);
        bundle.putInt("valor", valor);
        bundle.putString("tipo", tipo);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void validarAccion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SOLICITUD");
        builder.setMessage("¿Qué acción vas a realizar?");
        //builder.setCancelable(false);
        builder.setPositiveButton("REGISTRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("VER REGISTROS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PrimerNivelWelcomeGarita.this, TercerNivelListarRegistrosGarita.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNeutralButton("SINCRONIZAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                verificarRegistrosBus();
                verificarRegistrosBusIntermedio();
                verificarRegistrosPersonal();
                verificarRegistrosUnidad();

                if (validarBUS == 1 && validarBUSINTERMEDIO == 1){
                    registrarDatosBUS();
                    registrarDatosBUSINTERMEDIO();

                    actualizarEstadoSincronizacionGaritaNivelUno();
                    actualizarEstadoSincronizacionGaritaNivelDos();
                }

                if (validarPERSONAL == 1){
                    registrarDatosPERSONAL();
                    actualizarEstadoSincronizacionPersonalNivelUno();
                }

                if (validarUNIDAD == 1){
                    registrarDatosUNIDAD();
                    actualizarEstadoSincronizacionUnidadNivelUno();
                }

                if (validarBUS == 1 || validarBUSINTERMEDIO == 1 || validarPERSONAL == 1 || validarUNIDAD == 1){
                    Toast.makeText(PrimerNivelWelcomeGarita.this, "Data Migrada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PrimerNivelWelcomeGarita.this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.create().show();
    }

    private void verificarRegistrosBus() {
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1+"="+"'0'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                validarBUS = 1;
                arrayListIdBUS.add(cursor.getString(0));
                arrayListAnexoPlacaBUS.add(cursor.getString(1));
                arrayListZonaBUS.add(cursor.getString(2));
                arrayListFundoBUS.add(cursor.getString(3));
                arrayListPersonalBUS.add(cursor.getString(4));
                arrayListFechaBUS.add(cursor.getString(5));
                arrayListHoraBUS.add(cursor.getString(6));
                arrayListSincBUS.add(cursor.getString(7));
            }
        }else{
            validarBUS = 0;
        }

    }
    private void verificarRegistrosBusIntermedio() {
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1_5+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1_5+"="+"'0'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                validarBUSINTERMEDIO = 1;
                arrayListIdBUSINTERMEDIO.add(cursor.getString(0));
                arrayListAnexoPersonalBUSINTERMEDIO.add(cursor.getString(1));
                arrayListContadorBUSINTERMEDIO.add(cursor.getString(2));
                arrayListPlacaBUSINTERMEDIO.add(cursor.getString(3));
                arrayListSincBUSINTERMEDIO.add(cursor.getString(4));
            }
        }else{
            validarBUSINTERMEDIO = 0;
        }

    }
    private void verificarRegistrosPersonal() {
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL2+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL2+"="+"'0'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                validarPERSONAL = 1;
                arrayListIdPERSONAL.add(cursor.getString(0));
                arrayListZonaPERSONAL.add(cursor.getString(1));
                arrayListFundoPERSONAL.add(cursor.getString(2));
                arrayListPersonalPERSONAL.add(cursor.getString(3));
                arrayListTipoHoraPERSONAL.add(cursor.getString(4));
                arrayListFechaPERSONAL.add(cursor.getString(5));
                arrayListHoraPERSONAL.add(cursor.getString(6));
                arrayListSincPERSONAL.add(cursor.getString(7));
            }
        }else{
            validarPERSONAL = 0;
        }

    }
    private void verificarRegistrosUnidad() {
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL3+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL3+"="+"'0'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                validarUNIDAD = 1;
                arrayListIdUNIDAD.add(cursor.getString(0));
                arrayListZonaUNIDAD.add(cursor.getString(1));
                arrayListFundoUNIDAD.add(cursor.getString(2));
                arrayListPersonalUNIDAD.add(cursor.getString(3));
                arrayListTipoHoraUNIDAD.add(cursor.getString(4));
                arrayListFechaUNIDAD.add(cursor.getString(5));
                arrayListHoraUNIDAD.add(cursor.getString(6));
                arrayListSincUNIDAD.add(cursor.getString(7));
            }
        }else{
            validarUNIDAD = 0;
        }

    }

    private void registrarDatosBUS(){
        for (int i=0; i<arrayListIdBUS.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("placa",arrayListAnexoPlacaBUS.get(i));
                object.put("zona",arrayListZonaBUS.get(i));
                object.put("fundo",arrayListFundoBUS.get(i));
                object.put("personal",arrayListPersonalBUS.get(i));
                object.put("fecha",arrayListFechaBUS.get(i));
                object.put("hora",arrayListHoraBUS.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/garita_bus_uno", object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(PrimerNivelWelcomeGarita.this);
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void registrarDatosBUSINTERMEDIO(){
        for (int i=0; i<arrayListIdBUSINTERMEDIO.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("personal",arrayListAnexoPersonalBUSINTERMEDIO.get(i));
                object.put("contador",arrayListContadorBUSINTERMEDIO.get(i));
                object.put("anexo_placa",arrayListPlacaBUSINTERMEDIO.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/garita_bus_dos", object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PrimerNivelWelcomeGarita.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(PrimerNivelWelcomeGarita.this);
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void registrarDatosPERSONAL(){
        for (int i=0; i<arrayListIdPERSONAL.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("zona",arrayListZonaPERSONAL.get(i));
                object.put("fundo",arrayListFundoPERSONAL.get(i));
                object.put("personal",arrayListPersonalPERSONAL.get(i));
                object.put("tipo_hora",arrayListTipoHoraPERSONAL.get(i));
                object.put("fecha",arrayListFechaPERSONAL.get(i));
                object.put("hora",arrayListHoraPERSONAL.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/garita_personal", object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(PrimerNivelWelcomeGarita.this);
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void registrarDatosUNIDAD(){
        for (int i=0; i<arrayListIdUNIDAD.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("zona",arrayListZonaUNIDAD.get(i));
                object.put("fundo",arrayListFundoUNIDAD.get(i));
                object.put("personal",arrayListPersonalUNIDAD.get(i));
                object.put("tipo_hora",arrayListTipoHoraUNIDAD.get(i));
                object.put("fecha",arrayListFechaUNIDAD.get(i));
                object.put("hora",arrayListHoraUNIDAD.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/garita_unidad", object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(PrimerNivelWelcomeGarita.this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void actualizarEstadoSincronizacionGaritaNivelUno(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idBusNivelUno.add(cursorData.getString(0));
                }while (cursorData.moveToNext());

                for (int i=0; i<idBusNivelUno.size(); i++){
                    String [] parametro = {idBusNivelUno.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado","1");
                    database.update(Utilidades.TABLA_GARITA_NIVEL1, contentValuesActSincronizacion, Utilidades.CAMPO_GARITA_ID_NIVEL1+"=?", parametro);
                }
                cursorData.close();
            }
        }

    }
    private void actualizarEstadoSincronizacionGaritaNivelDos(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1_5+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1_5+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idBusNivelDos.add(cursorData.getString(0));
                }while (cursorData.moveToNext());

                for (int i=0; i<idBusNivelDos.size(); i++){
                    String [] parametro = {idBusNivelDos.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado","1");
                    database.update(Utilidades.TABLA_GARITA_NIVEL1_5, contentValuesActSincronizacion, Utilidades.CAMPO_GARITA_ID_NIVEL1_5+"=?", parametro);
                }
                cursorData.close();
            }
        }

    }
    private void actualizarEstadoSincronizacionPersonalNivelUno(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL2+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL2+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idPersonalNivelUno.add(cursorData.getString(0));
                }while (cursorData.moveToNext());

                for (int i=0; i<idPersonalNivelUno.size(); i++){
                    String [] parametro = {idPersonalNivelUno.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado","1");
                    database.update(Utilidades.TABLA_GARITA_NIVEL2, contentValuesActSincronizacion, Utilidades.CAMPO_GARITA_ID_NIVEL2+"=?", parametro);
                }
                cursorData.close();
            }
        }

    }
    private void actualizarEstadoSincronizacionUnidadNivelUno(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL3+" WHERE "+Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL3+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idUnidadNivelUno.add(cursorData.getString(0));
                }while (cursorData.moveToNext());

                for (int i=0; i<idUnidadNivelUno.size(); i++){
                    String [] parametro = {idUnidadNivelUno.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado","1");
                    database.update(Utilidades.TABLA_GARITA_NIVEL3, contentValuesActSincronizacion, Utilidades.CAMPO_GARITA_ID_NIVEL3+"=?", parametro);
                }
                cursorData.close();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sincronizar_action:
                validarAccion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PrimerNivelWelcomeGarita.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}