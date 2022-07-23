package com.example.agroathos.RRHH_TAREO_AR_PLANTA;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_Actividades;
import com.example.agroathos.ENTIDADES.E_Labores_Planta;
import com.example.agroathos.ENTIDADES.E_Mesas;
import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.ENTIDADES.E_Procesos;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListarPersonalTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelRegistrarGrupoTrabajo;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorActividades;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorLaboresPlanta;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorListarPersonalTrabajoPlanta;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorMesas;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorProcesos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class TercerNivelWelcomeTareoPlanta extends AppCompatActivity {

    Spinner spProceso, spActividad, spLabor, spMesa;
    Button btnRegistrar;
    ListView lvPersonal;
    FloatingActionButton fabCamara;
    ConstraintLayout layout;

    ArrayList<E_Procesos> arrayProcesos = new ArrayList<>();
    ArrayList<E_Actividades> arrayActividades = new ArrayList<>();
    ArrayList<E_Labores_Planta> arrayLabores = new ArrayList<>();
    ArrayList<E_Mesas> arrayMesas = new ArrayList<>();

    AdaptadorProcesos adaptadorProcesos;
    AdaptadorActividades adaptadorActividades;
    AdaptadorLaboresPlanta adaptadorLaboresPlanta;
    AdaptadorMesas adaptadorMesas;

    //DATOS A REGISTRAR
    String proceso = "";
    String actividad = "";
    String labor = "";
    String mesa = "";

    //DATOS RECIBIDOS DEL NIVEL1
    String idNivel1 = "";
    String linea = "";
    String dni = "";

    //VARIABLE UNICA
    String idGrupo = "";

    ConexionSQLiteHelper conn;
    ContentValues valuesGrupo = new ContentValues();
    ContentValues valuesProceso = new ContentValues();

    ArrayList<E_PersonalTrabajo> personalTrabajoArrayList = new ArrayList<>();
    AdaptadorListarPersonalTrabajoPlanta adaptadorListarPersonalTrabajo;
    ArrayList<String> arrayPersonal = new ArrayList<>();
    String valorPersonal = "";
    TextToSpeech voz;
    int contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_welcome_tareo_planta);

        conn = new ConexionSQLiteHelper(this, "athos0", null, Utilidades.VERSION_APP);

        SharedPreferences preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);
        dni = preferences.getString("dni","");
        linea = preferences.getString("linea","");
        idNivel1 = preferences.getString("idNivel1","");

        voz = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voz.setLanguage(new Locale("es", "pe"));
                }
            }
        });

        spProceso = findViewById(R.id.spProcesoRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL);
        spActividad = findViewById(R.id.spActividadRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL);
        spLabor = findViewById(R.id.spLaborRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL);
        spMesa = findViewById(R.id.spMesaRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL);
        btnRegistrar = findViewById(R.id.btnRegistrarRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL);
        lvPersonal = findViewById(R.id.lvPersonalRRHH_TAREO_ARANDANO_PLANTA);
        fabCamara = findViewById(R.id.fabCamaraRRHH_TAREO_ARPLANTA);
        layout = findViewById(R.id.clPrincipalRRHH_TAREO_AR_PLANTA_TERCER_NIVEL);

        recibirLinea();

        spProceso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Procesos e_procesos = arrayProcesos.get(position);
                proceso = e_procesos.getNombre();

                switch (linea){
                    case "FGR01 - GRANADA":
                        switch (proceso){
                            case "PROD - LINEA 01":
                            case "PROD - LINEA 02":
                            case "PROD - LINEA 03":
                            case "PROD - LINEA 04":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("CLAS - SELECCIÓN Y CLASIFICACION"));
                                arrayActividades.add(new E_Actividades("EMPP - EMPAQUE Y PALETIZADO"));
                                break;
                            case "GENERAL":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("ALMA - ALMACEN Y DESPACHO PPTT"));
                                arrayActividades.add(new E_Actividades("RECE - RECEPCION Y ACOPIO"));
                                arrayActividades.add(new E_Actividades("REEM - REEMPAQUE"));
                                break;
                        }
                        break;
                    case "FAR01 - ARANDANO":
                        switch (proceso){
                            case "PROD - LINEA 01":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("CLAS - SELECCIÓN Y CLASIFICACION"));
                                arrayActividades.add(new E_Actividades("EMPP - EMPAQUE Y PALETIZADO"));
                                break;
                            case "GENERAL":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("ALMA - ALMACEN Y DESPACHO PPTT"));
                                arrayActividades.add(new E_Actividades("RECE - RECEPCION Y ACOPIO"));
                                arrayActividades.add(new E_Actividades("REEM - REEMPAQUE"));
                                break;
                        }
                        break;
                    case "IND - INDIRECTO":
                        switch (proceso){
                            case "IND PRODUCCION":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("AUXPRD - AUXILIAR DE PRODUCCION"));
                                break;
                            case "IND CALIDAD":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("AUXCAL - AUXILIAR DE CALIDAD"));
                                break;
                            case "IND ACOPIO":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("CHF001 - CHOFERES"));
                                arrayActividades.add(new E_Actividades("AUXPRD - AUXILIAR DE PRODUCCION"));
                                break;
                            case "IND SANEAMIENTO":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("AUXPRD - AUXILIAR DE PRODUCCION"));
                                arrayActividades.add(new E_Actividades("LIMPLT - LIMPIEZA PLANTA"));
                                break;
                            case "IND SOPORTE":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
                                arrayActividades.add(new E_Actividades("ALM001 - ALMACEN"));
                                arrayActividades.add(new E_Actividades("MNT001 - MANTENIMIENTO"));
                                arrayActividades.add(new E_Actividades("VIG001 - VIGILANTES"));
                                break;
                        }
                        break;
                }

                adaptadorActividades = new AdaptadorActividades(TercerNivelWelcomeTareoPlanta.this, arrayActividades);
                spActividad.setAdapter(adaptadorActividades);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Actividades e_actividades = arrayActividades.get(position);
                actividad = e_actividades.getNombre();

                switch (linea){
                    case "FGR01 - GRANADA":
                        switch (proceso){
                            case "PROD - LINEA 01":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                        break;
                                }
                            case "PROD - LINEA 02":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                        break;
                                }
                            case "PROD - LINEA 03":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                        break;
                                }
                            case "PROD - LINEA 04":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                        break;
                                }
                                break;
                            case "GENERAL":
                                switch (actividad){
                                    case "ALMA - ALMACEN Y DESPACHO PPTT":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Despacho"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Cámara"));
                                        break;
                                    case "RECE - RECEPCION Y ACOPIO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Pesador"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Acopio"));
                                        break;
                                    case "REEM - REEMPAQUE":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Reempaque"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "FAR01 - ARANDANO":
                        switch (proceso){
                            case "PROD - LINEA 01":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                        break;
                                }
                                break;
                            case "GENERAL":
                                switch (actividad){
                                    case "ALMA - ALMACEN Y DESPACHO PPTT":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Despacho"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Cámara"));
                                        break;
                                    case "RECE - RECEPCION Y ACOPIO":
                                    case "REEM - REEMPAQUE":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Pesador"));
                                        arrayLabores.add(new E_Labores_Planta("PMODI1 - Acopio"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "IND - INDIRECTO":
                        switch (proceso){
                            case "IND PRODUCCION":
                                switch (actividad){
                                    case "AUXPRD - AUXILIAR DE PRODUCCION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("AUXPRD - Empaque"));
                                        arrayLabores.add(new E_Labores_Planta("AUXPRD - Cámara"));
                                        break;
                                }
                                break;
                            case "IND CALIDAD":
                                switch (actividad){
                                    case "AUXCAL - AUXILIAR DE CALIDAD":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("AUXCAL - Recepción"));
                                        arrayLabores.add(new E_Labores_Planta("AUXCAL - Trazabilidad"));
                                        arrayLabores.add(new E_Labores_Planta("AUXCAL - Paletizado"));
                                        break;
                                }
                                break;
                            case "IND ACOPIO":
                                switch (actividad){
                                    case "CHF001 - CHOFERES":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("CHF001 - Choferes"));
                                        break;
                                    case "AUXPRD - AUXILIAR DE PRODUCCION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("AUXPRD - Acopio"));
                                        break;
                                }
                                break;
                            case "IND SANEAMIENTO":
                                switch (actividad){
                                    case "AUXPRD - AUXILIAR DE PRODUCCION":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("AUXPRD - Saneamiento"));
                                        break;
                                    case "LIMPLT - LIMPIEZA PLANTA":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("LIMPLT - Limpieza Planta"));
                                        break;
                                }
                                break;
                            case "IND SOPORTE":
                                switch (actividad){
                                    case "ALM001 - ALMACEN":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("ALM001 - Almacén"));
                                        break;
                                    case "MNT001 - MANTENIMIENTO":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("MNT001 - Mantenimiento"));
                                        break;
                                    case "VIG001 - VIGILANTES":
                                        arrayLabores.clear();
                                        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
                                        arrayLabores.add(new E_Labores_Planta("VIG001 - Vigilantes"));
                                        break;
                                }
                                break;
                        }
                        break;
                }

                adaptadorLaboresPlanta = new AdaptadorLaboresPlanta(TercerNivelWelcomeTareoPlanta.this, arrayLabores);
                spLabor.setAdapter(adaptadorLaboresPlanta);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLabor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Labores_Planta e_labores_planta = arrayLabores.get(position);
                labor = e_labores_planta.getNombre();

                switch (linea){
                    case "FGR01 - GRANADA":
                        switch (proceso){
                            case "PROD - LINEA 01":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        switch (labor){
                                            case "PMODI1 - Lanzado":
                                            case "PMODI1 - Selección":
                                            case "PMODI1 - Clasificación":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        switch (labor){
                                            case "PMODI1 - Empaque":
                                            case "PMODI1 - Etiquetado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("MESA 01"));
                                                arrayMesas.add(new E_Mesas("MESA 02"));
                                                arrayMesas.add(new E_Mesas("MESA 03"));
                                                arrayMesas.add(new E_Mesas("MESA 04"));
                                                arrayMesas.add(new E_Mesas("MESA 05"));
                                                arrayMesas.add(new E_Mesas("MESA 06"));
                                                arrayMesas.add(new E_Mesas("MESA 07"));
                                                arrayMesas.add(new E_Mesas("MESA 08"));
                                                arrayMesas.add(new E_Mesas("MESA 09"));
                                                arrayMesas.add(new E_Mesas("MESA 10"));
                                                break;
                                            case "PMODI1 - Paletizado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                            case "PROD - LINEA 02":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        switch (labor){
                                            case "PMODI1 - Lanzado":
                                            case "PMODI1 - Selección":
                                            case "PMODI1 - Clasificación":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        switch (labor){
                                            case "PMODI1 - Empaque":
                                            case "PMODI1 - Etiquetado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("MESA 01"));
                                                arrayMesas.add(new E_Mesas("MESA 02"));
                                                arrayMesas.add(new E_Mesas("MESA 03"));
                                                arrayMesas.add(new E_Mesas("MESA 04"));
                                                arrayMesas.add(new E_Mesas("MESA 05"));
                                                arrayMesas.add(new E_Mesas("MESA 06"));
                                                arrayMesas.add(new E_Mesas("MESA 07"));
                                                arrayMesas.add(new E_Mesas("MESA 08"));
                                                arrayMesas.add(new E_Mesas("MESA 09"));
                                                arrayMesas.add(new E_Mesas("MESA 10"));
                                                break;
                                            case "PMODI1 - Paletizado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                            case "PROD - LINEA 03":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        switch (labor){
                                            case "PMODI1 - Lanzado":
                                            case "PMODI1 - Selección":
                                            case "PMODI1 - Clasificación":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        switch (labor){
                                            case "PMODI1 - Empaque":
                                            case "PMODI1 - Etiquetado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("MESA 01"));
                                                arrayMesas.add(new E_Mesas("MESA 02"));
                                                arrayMesas.add(new E_Mesas("MESA 03"));
                                                arrayMesas.add(new E_Mesas("MESA 04"));
                                                arrayMesas.add(new E_Mesas("MESA 05"));
                                                arrayMesas.add(new E_Mesas("MESA 06"));
                                                arrayMesas.add(new E_Mesas("MESA 07"));
                                                arrayMesas.add(new E_Mesas("MESA 08"));
                                                arrayMesas.add(new E_Mesas("MESA 09"));
                                                arrayMesas.add(new E_Mesas("MESA 10"));
                                                break;
                                            case "PMODI1 - Paletizado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                            case "PROD - LINEA 04":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        switch (labor){
                                            case "PMODI1 - Lanzado":
                                            case "PMODI1 - Selección":
                                            case "PMODI1 - Clasificación":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        switch (labor){
                                            case "PMODI1 - Empaque":
                                            case "PMODI1 - Etiquetado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("MESA 01"));
                                                arrayMesas.add(new E_Mesas("MESA 02"));
                                                arrayMesas.add(new E_Mesas("MESA 03"));
                                                arrayMesas.add(new E_Mesas("MESA 04"));
                                                arrayMesas.add(new E_Mesas("MESA 05"));
                                                arrayMesas.add(new E_Mesas("MESA 06"));
                                                arrayMesas.add(new E_Mesas("MESA 07"));
                                                arrayMesas.add(new E_Mesas("MESA 08"));
                                                arrayMesas.add(new E_Mesas("MESA 09"));
                                                arrayMesas.add(new E_Mesas("MESA 10"));
                                                break;
                                            case "PMODI1 - Paletizado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "GENERAL":
                                switch (actividad){
                                    case "ALMA - ALMACEN Y DESPACHO PPTT":
                                        switch (labor){
                                            case "PMODI1 - Despacho":
                                            case "PMODI1 - Cámara":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "RECE - RECEPCION Y ACOPIO":
                                        switch (labor){
                                            case "PMODI1 - Pesador":
                                            case "PMODI1 - Acopio":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "REEM - REEMPAQUE":
                                        switch (labor){
                                            case "PMODI1 - Reempaque":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                    case "FAR01 - ARANDANO":
                        switch (proceso){
                            case "PROD - LINEA 01":
                                switch (actividad){
                                    case "CLAS - SELECCIÓN Y CLASIFICACION":
                                        switch (labor){
                                            case "PMODI1 - Lanzado":
                                            case "PMODI1 - Selección":
                                            case "PMODI1 - Clasificación":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "EMPP - EMPAQUE Y PALETIZADO":
                                        switch (labor){
                                            case "PMODI1 - Empaque":
                                            case "PMODI1 - Etiquetado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("MESA 01"));
                                                arrayMesas.add(new E_Mesas("MESA 02"));
                                                arrayMesas.add(new E_Mesas("MESA 03"));
                                                arrayMesas.add(new E_Mesas("MESA 04"));
                                                arrayMesas.add(new E_Mesas("MESA 05"));
                                                arrayMesas.add(new E_Mesas("MESA 06"));
                                                arrayMesas.add(new E_Mesas("MESA 07"));
                                                arrayMesas.add(new E_Mesas("MESA 08"));
                                                arrayMesas.add(new E_Mesas("MESA 09"));
                                                arrayMesas.add(new E_Mesas("MESA 10"));
                                                break;
                                            case "PMODI1 - Paletizado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "GENERAL":
                                switch (actividad){
                                    case "ALMA - ALMACEN Y DESPACHO PPTT":
                                        switch (labor){
                                            case "PMODI1 - Despacho":
                                            case "PMODI1 - Cámara":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "RECE - RECEPCION Y ACOPIO":
                                    case "REEM - REEMPAQUE":
                                        switch (labor){
                                            case "PMODI1 - Pesador":
                                            case "PMODI1 - Acopio":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                    case "IND - INDIRECTO":
                        switch (proceso){
                            case "IND PRODUCCION":
                                switch (actividad){
                                    case "AUXPRD - AUXILIAR DE PRODUCCION":
                                        switch (labor){
                                            case "AUXPRD - Empaque":
                                            case "AUXPRD - Cámara":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "IND CALIDAD":
                                switch (actividad){
                                    case "AUXCAL - AUXILIAR DE CALIDAD":
                                        switch (labor){
                                            case "AUXCAL - Recepción":
                                            case "AUXCAL - Trazabilidad":
                                            case "AUXCAL - Paletizado":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "IND ACOPIO":
                                switch (actividad){
                                    case "CHF001 - CHOFERES":
                                        switch (labor){
                                            case "CHF001 - Choferes":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "AUXPRD - AUXILIAR DE PRODUCCION":
                                        switch (labor){
                                            case "AUXPRD - Acopio":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "IND SANEAMIENTO":
                                switch (actividad){
                                    case "AUXPRD - AUXILIAR DE PRODUCCION":
                                        switch (labor){
                                            case "AUXPRD - Saneamiento":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "LIMPLT - LIMPIEZA PLANTA":
                                        switch (labor){
                                            case "LIMPLT - Limpieza Planta":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "IND SOPORTE":
                                switch (actividad){
                                    case "ALM001 - ALMACEN":
                                        switch (labor){
                                            case "ALM001 - Almacén":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "MNT001 - MANTENIMIENTO":
                                        switch (labor){
                                            case "MNT001 - Mantenimiento":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                    case "VIG001 - VIGILANTES":
                                        switch (labor){
                                            case "VIG001 - Vigilantes":
                                                arrayMesas.clear();
                                                arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
                                                arrayMesas.add(new E_Mesas("No Aplica"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }

                adaptadorMesas = new AdaptadorMesas(TercerNivelWelcomeTareoPlanta.this, arrayMesas);
                spMesa.setAdapter(adaptadorMesas);

                if (actividad.equals("EMPP - EMPAQUE Y PALETIZADO")){
                    if (labor.equals("PMODI1 - Empaque") || labor.equals("PMODI1 - Etiquetado")){
                        spMesa.setVisibility(View.VISIBLE);
                    }else{
                        mesa = "No Aplica";
                        spMesa.setVisibility(View.GONE);
                    }
                }else{
                    mesa = "No Aplica";
                    spMesa.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Mesas e_mesas = arrayMesas.get(position);
                mesa = e_mesas.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegistrar.setOnClickListener(view -> registrarDatos());
        fabCamara.setOnClickListener(view -> iniciarScan());
    }

    public void recibirLinea(){
        arrayProcesos.add(new E_Procesos("-- Selecciona un Proceso --"));
        arrayActividades.add(new E_Actividades("-- Selecciona una Actividad --"));
        arrayLabores.add(new E_Labores_Planta("-- Selecciona una Labor --"));
        arrayMesas.add(new E_Mesas("-- Selecciona una Mesa --"));
        switch (linea){
            case "FGR01 - GRANADA":
                arrayProcesos.clear();
                arrayProcesos.add(new E_Procesos("-- Selecciona un Proceso --"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 01"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 02"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 03"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 04"));
                arrayProcesos.add(new E_Procesos("GENERAL"));
                break;
            case "FAR01 - ARANDANO":
                arrayProcesos.clear();
                arrayProcesos.add(new E_Procesos("-- Selecciona un Proceso --"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 01"));
                arrayProcesos.add(new E_Procesos("GENERAL"));
                break;
            case "IND - INDIRECTO":
                arrayProcesos.clear();
                arrayProcesos.add(new E_Procesos("-- Selecciona un Proceso --"));
                arrayProcesos.add(new E_Procesos("IND PRODUCCION"));
                arrayProcesos.add(new E_Procesos("IND CALIDAD"));
                arrayProcesos.add(new E_Procesos("IND ACOPIO"));
                arrayProcesos.add(new E_Procesos("IND SANEAMIENTO"));
                arrayProcesos.add(new E_Procesos("IND SOPORTE"));
                break;
        }

        adaptadorProcesos = new AdaptadorProcesos(TercerNivelWelcomeTareoPlanta.this, arrayProcesos);
        spProceso.setAdapter(adaptadorProcesos);
    }

    private void iniciarScan(){
        IntentIntegrator integrator = new IntentIntegrator(TercerNivelWelcomeTareoPlanta.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("LECTOR QR PERSONAL");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void registrarDatos(){

        if (proceso.equals("-- Selecciona un Proceso --")){
            Snackbar.make(layout, "Selecciona un Proceso", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (actividad.equals("-- Selecciona una Actividad --")){
            Snackbar.make(layout, "Selecciona una Actividad", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (labor.equals("-- Selecciona una Labor --")){
            Snackbar.make(layout, "Selecciona una Labor", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (mesa.equals("-- Selecciona una Mesa --")){
            Snackbar.make(layout, "Selecciona una Mesa", Snackbar.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL2 + " WHERE "
                + Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL2 + "=" + "'"+dni+"'", null);

        idGrupo = UUID.randomUUID().toString();

        if (cursor.getCount()<0){
            valuesGrupo.put(Utilidades.CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2, idNivel1);
            valuesGrupo.put(Utilidades.CAMPO_ID_GRUPO_TAREO_PLANTA_NIVEL2, idGrupo);
            valuesGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_TAREO_PLANTA_NIVEL2, 1);
            valuesGrupo.put(Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL2, dni);
            valuesGrupo.put(Utilidades.CAMPO_ESTADO_TAREO_PLANTA_NIVEL2, "ABIERTO");
            valuesGrupo.put(Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL2, "0");
        }else{
            valuesGrupo.put(Utilidades.CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2, idNivel1);
            valuesGrupo.put(Utilidades.CAMPO_ID_GRUPO_TAREO_PLANTA_NIVEL2, idGrupo);
            valuesGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_TAREO_PLANTA_NIVEL2, cursor.getCount()+1);
            valuesGrupo.put(Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL2, dni);
            valuesGrupo.put(Utilidades.CAMPO_ESTADO_TAREO_PLANTA_NIVEL2, "ABIERTO");
            valuesGrupo.put(Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL2, "0");
        }

        database.insert(Utilidades.TABLA_TAREO_PLANTA_NIVEL2, Utilidades.CAMPO_ID_TAREO_PLANTA_NIVEL2, valuesGrupo);

        for (int i=0; i<lvPersonal.getCount(); i++){
            valuesProceso.put(Utilidades.CAMPO_ANEXO_GRUPO_TAREO_PLANTA_NIVEL3, idGrupo);
            valuesProceso.put(Utilidades.CAMPO_PROCESO_TAREO_PLANTA_NIVEL3, proceso);
            valuesProceso.put(Utilidades.CAMPO_ACTIVIDAD_TAREO_PLANTA_NIVEL3, actividad);
            valuesProceso.put(Utilidades.CAMPO_LABOR_TAREO_PLANTA_NIVEL3, labor);
            valuesProceso.put(Utilidades.CAMPO_MESA_TAREO_PLANTA_NIVEL3, mesa);
            valuesProceso.put(Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL3, dni);
            valuesProceso.put(Utilidades.CAMPO_QRPERSONAL_TAREO_PLANTA_NIVEL3, arrayPersonal.get(i));
            valuesProceso.put(Utilidades.CAMPO_FECHA_TAREO_PLANTA_NIVEL3, obtenerFechaActual("AMERICA/Lima"));
            valuesProceso.put(Utilidades.CAMPO_HORA_TAREO_PLANTA_NIVEL3, obtenerHoraActual("GMT-5"));
            valuesProceso.put(Utilidades.CAMPO_ESTADO_TAREO_PLANTA_NIVEL3, "ABIERTO");
            valuesProceso.put(Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL3, "0");
            Long idResultante = database.insert(Utilidades.TABLA_TAREO_PLANTA_NIVEL3, Utilidades.CAMPO_ID_TAREO_PLANTA_NIVEL3, valuesProceso);

            if (idResultante > 0){
                Toast.makeText(this, "Datos Registrados!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TercerNivelWelcomeTareoPlanta.this, SegundoNivelWelcomeTareoPlanta.class);
                startActivity(intent);
            }
        }

    }

    public static String obtenerHoraActual(String zonaHoraria) {
        String formato = "HH:mm:ss";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }
    public static String obtenerFechaActual(String zonaHoraria) {
        String formato = "dd-MM-yyyy";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }
    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            String scanFormat = intentResult.getFormatName();

            if (!TextUtils.isEmpty(scanFormat)){
                if (scanFormat.equals("QR_CODE") || scanFormat.equals("CODE_39")){
                    if (intentResult.getContents() == null){
                        if (valorPersonal.isEmpty()){
                            //Toast.makeText(SegundoNivelRegistrarGrupoTrabajo.this, "Listando los registros", Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(TercerNivelWelcomeTareoPlanta.this);
                            builder.setTitle("ERROR");
                            builder.setMessage("Te faltaron llenar datos.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    valorPersonal = "";
                                    arrayPersonal.clear();
                                    iniciarScan();
                                }
                            });
                            builder.create().show();
                        }
                        adaptadorListarPersonalTrabajo = new AdaptadorListarPersonalTrabajoPlanta(TercerNivelWelcomeTareoPlanta.this,
                                personalTrabajoArrayList);
                        lvPersonal.setAdapter(adaptadorListarPersonalTrabajo);

                    }else{
                        if (valorPersonal.isEmpty()){
                            if (arrayPersonal.contains(intentResult.getContents())){
                                AlertDialog.Builder builder = new AlertDialog.Builder(TercerNivelWelcomeTareoPlanta.this);
                                builder.setTitle("ERROR");
                                builder.setMessage("El personal ya se encuentra en la lista.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        iniciarScan();
                                    }
                                });
                                builder.create().show();
                            }else{
                                try {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);

                                    if (intentResult.getContents().length() > 8){
                                        String dato = intentResult.getContents().substring(11);
                                        voz.speak(dato, TextToSpeech.QUEUE_FLUSH, bundle, null);
                                        arrayPersonal.add(intentResult.getContents());
                                        valorPersonal = intentResult.getContents();
                                        personalTrabajoArrayList.add(new E_PersonalTrabajo(String.valueOf(contador++), valorPersonal));
                                        valorPersonal = "";
                                        iniciarScan();
                                    }else{
                                        voz.speak("REGISTRADO", TextToSpeech.QUEUE_FLUSH, bundle, null);
                                        arrayPersonal.add(intentResult.getContents());
                                        valorPersonal = intentResult.getContents();
                                        personalTrabajoArrayList.add(new E_PersonalTrabajo(String.valueOf(contador++), valorPersonal));
                                        valorPersonal = "";
                                        iniciarScan();
                                    }

                                }catch (Exception e){
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }else{
                adaptadorListarPersonalTrabajo = new AdaptadorListarPersonalTrabajoPlanta(TercerNivelWelcomeTareoPlanta.this,
                        personalTrabajoArrayList);
                lvPersonal.setAdapter(adaptadorListarPersonalTrabajo);
            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setCancelable(false);

        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TercerNivelWelcomeTareoPlanta.super.onBackPressed();
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}