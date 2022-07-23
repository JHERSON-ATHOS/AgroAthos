package com.example.agroathos.RRHH_TAREO_AR_PLANTA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_Actividades;
import com.example.agroathos.ENTIDADES.E_Labores_Planta;
import com.example.agroathos.ENTIDADES.E_Mesas;
import com.example.agroathos.ENTIDADES.E_Procesos;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;
import com.example.agroathos.RRHH_TAREO_AR.TercerNivelConfiguracionGrupo;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorActividades;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorLaboresPlanta;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorMesas;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorProcesos;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TercerNivelWelcomeTareoPlantaEdicion extends AppCompatActivity {

    Spinner spProceso, spActividad, spLabor, spMesa;
    Button btnActualizar;
    ConstraintLayout layout;

    ArrayList<E_Procesos> arrayProcesos = new ArrayList<>();
    ArrayList<E_Actividades> arrayActividades = new ArrayList<>();
    ArrayList<E_Labores_Planta> arrayLabores = new ArrayList<>();
    ArrayList<E_Mesas> arrayMesas = new ArrayList<>();

    AdaptadorProcesos adaptadorProcesos;
    AdaptadorActividades adaptadorActividades;
    AdaptadorLaboresPlanta adaptadorLaboresPlanta;
    AdaptadorMesas adaptadorMesas;

    //DATOS A ACTUALIZAR
    String proceso = "";
    String procesoACT = "";
    String actividad = "";
    String actividadACT = "";
    String labor = "";
    String laborACT = "";
    String mesa = "";
    String mesaACT = "";

    //DATOS RECIBIDOS DEL NIVEL2
    String idGrupo = "";
    String idAnexoNivel1 = "";

    //DATOS BD
    String lineaBD = "";

    ConexionSQLiteHelper conn;
    ContentValues valuesGrupo = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_welcome_tareo_planta_edicion);

        conn = new ConexionSQLiteHelper(this, "athos0", null, Utilidades.VERSION_APP);

        Bundle bundle = getIntent().getExtras();
        idGrupo = bundle.getString("idGrupo");
        idAnexoNivel1 = bundle.getString("idAnexoNivel1");

        spProceso = findViewById(R.id.spProcesoRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL_EDITADO);
        spActividad = findViewById(R.id.spActividadRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL_EDITADO);
        spLabor = findViewById(R.id.spLaborRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL_EDITADO);
        spMesa = findViewById(R.id.spMesaRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL_EDITADO);
        btnActualizar = findViewById(R.id.btnRegistrarRRHH_TAREO_ARANDANO_PLANTA_TERCER_NIVEL_EDITADO);
        layout = findViewById(R.id.clPrincipalEditadoRRHH_TAREO_AR_PLANTA_TERCER_NIVEL);

        obtenerDatos(idGrupo);
        obtenerLinea(idAnexoNivel1);
        llenarSpiners();

        spProceso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Procesos e_procesos = arrayProcesos.get(position);
                procesoACT = e_procesos.getNombre();

                switch (lineaBD){
                    case "FGR01 - GRANADA":
                        switch (procesoACT){
                            case "PROD - LINEA 01":
                            case "PROD - LINEA 02":
                            case "PROD - LINEA 03":
                            case "PROD - LINEA 04":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("CLAS - SELECCIÓN Y CLASIFICACION"));
                                arrayActividades.add(new E_Actividades("EMPP - EMPAQUE Y PALETIZADO"));
                                break;
                            case "GENERAL":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades("ALMA - ALMACEN Y DESPACHO PPTT"));
                                arrayActividades.add(new E_Actividades("RECE - RECEPCION Y ACOPIO"));
                                arrayActividades.add(new E_Actividades("REEM - REEMPAQUE"));
                                break;
                        }
                        break;
                    case "FAR01 - ARANDANO":
                        switch (procesoACT){
                            case "PROD - LINEA 01":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("CLAS - SELECCIÓN Y CLASIFICACION"));
                                arrayActividades.add(new E_Actividades("EMPP - EMPAQUE Y PALETIZADO"));
                                break;
                            case "GENERAL":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("ALMA - ALMACEN Y DESPACHO PPTT"));
                                arrayActividades.add(new E_Actividades("RECE - RECEPCION Y ACOPIO"));
                                arrayActividades.add(new E_Actividades("REEM - REEMPAQUE"));
                                break;
                        }
                        break;
                    case "IND - INDIRECTO":
                        switch (procesoACT){
                            case "IND PRODUCCION":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("AUXPRD - AUXILIAR DE PRODUCCION"));
                                break;
                            case "IND CALIDAD":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("AUXCAL - AUXILIAR DE CALIDAD"));
                                break;
                            case "IND ACOPIO":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("CHF001 - CHOFERES"));
                                arrayActividades.add(new E_Actividades("AUXPRD - AUXILIAR DE PRODUCCION"));
                                break;
                            case "IND SANEAMIENTO":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("AUXPRD - AUXILIAR DE PRODUCCION"));
                                arrayActividades.add(new E_Actividades("LIMPLT - LIMPIEZA PLANTA"));
                                break;
                            case "IND SOPORTE":
                                arrayActividades.clear();
                                arrayActividades.add(new E_Actividades(actividad));
                                arrayActividades.add(new E_Actividades("ALM001 - ALMACEN"));
                                arrayActividades.add(new E_Actividades("MNT001 - MANTENIMIENTO"));
                                arrayActividades.add(new E_Actividades("VIG001 - VIGILANTES"));
                                break;
                        }
                        break;
                }

                adaptadorActividades = new AdaptadorActividades(TercerNivelWelcomeTareoPlantaEdicion.this, arrayActividades);
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
                actividadACT = e_actividades.getNombre();

                switch (procesoACT){
                    case "PROD - LINEA 01":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                break;
                        }
                    case "PROD - LINEA 02":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                break;
                        }
                    case "PROD - LINEA 03":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                break;
                        }
                    case "PROD - LINEA 04":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                break;
                        }
                        break;
                    case "GENERAL":
                        switch (actividadACT){
                            case "ALMA - ALMACEN Y DESPACHO PPTT":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Despacho"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Cámara"));
                                break;
                            case "RECE - RECEPCION Y ACOPIO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Pesador"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Acopio"));
                                break;
                            case "REEM - REEMPAQUE":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Reempaque"));
                                break;
                        }
                        break;
                }
                switch (procesoACT){
                    case "PROD - LINEA 01":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Lanzado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Selección"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Clasificación"));
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Empaque"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Etiquetado"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Paletizado"));
                                break;
                        }
                        break;
                    case "GENERAL":
                        switch (actividadACT){
                            case "ALMA - ALMACEN Y DESPACHO PPTT":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Despacho"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Cámara"));
                                break;
                            case "RECE - RECEPCION Y ACOPIO":
                            case "REEM - REEMPAQUE":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Pesador"));
                                arrayLabores.add(new E_Labores_Planta("PMODI1 - Acopio"));
                                break;
                        }
                        break;
                }
                switch (procesoACT){
                    case "IND PRODUCCION":
                        switch (actividadACT){
                            case "AUXPRD - AUXILIAR DE PRODUCCION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("AUXPRD - Empaque"));
                                arrayLabores.add(new E_Labores_Planta("AUXPRD - Cámara"));
                                break;
                        }
                        break;
                    case "IND CALIDAD":
                        switch (actividadACT){
                            case "AUXCAL - AUXILIAR DE CALIDAD":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("AUXCAL - Recepción"));
                                arrayLabores.add(new E_Labores_Planta("AUXCAL - Trazabilidad"));
                                arrayLabores.add(new E_Labores_Planta("AUXCAL - Paletizado"));
                                break;
                        }
                        break;
                    case "IND ACOPIO":
                        switch (actividadACT){
                            case "CHF001 - CHOFERES":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("CHF001 - Choferes"));
                                break;
                            case "AUXPRD - AUXILIAR DE PRODUCCION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("AUXPRD - Acopio"));
                                break;
                        }
                        break;
                    case "IND SANEAMIENTO":
                        switch (actividadACT){
                            case "AUXPRD - AUXILIAR DE PRODUCCION":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("AUXPRD - Saneamiento"));
                                break;
                            case "LIMPLT - LIMPIEZA PLANTA":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("LIMPLT - Limpieza Planta"));
                                break;
                        }
                        break;
                    case "IND SOPORTE":
                        switch (actividadACT){
                            case "ALM001 - ALMACEN":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("ALM001 - Almacén"));
                                break;
                            case "MNT001 - MANTENIMIENTO":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("MNT001 - Mantenimiento"));
                                break;
                            case "VIG001 - VIGILANTES":
                                arrayLabores.clear();
                                arrayLabores.add(new E_Labores_Planta(labor));
                                arrayLabores.add(new E_Labores_Planta("VIG001 - Vigilantes"));
                                break;
                        }
                        break;
                }

                adaptadorLaboresPlanta = new AdaptadorLaboresPlanta(TercerNivelWelcomeTareoPlantaEdicion.this, arrayLabores);
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
                laborACT = e_labores_planta.getNombre();

                switch (procesoACT){
                    case "PROD - LINEA 01":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                switch (laborACT){
                                    case "PMODI1 - Lanzado":
                                    case "PMODI1 - Selección":
                                    case "PMODI1 - Clasificación":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                switch (laborACT){
                                    case "PMODI1 - Empaque":
                                    case "PMODI1 - Etiquetado":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
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
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                    case "PROD - LINEA 02":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                switch (laborACT){
                                    case "PMODI1 - Lanzado":
                                    case "PMODI1 - Selección":
                                    case "PMODI1 - Clasificación":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                switch (laborACT){
                                    case "PMODI1 - Empaque":
                                    case "PMODI1 - Etiquetado":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
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
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                    case "PROD - LINEA 03":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                switch (laborACT){
                                    case "PMODI1 - Lanzado":
                                    case "PMODI1 - Selección":
                                    case "PMODI1 - Clasificación":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                switch (laborACT){
                                    case "PMODI1 - Empaque":
                                    case "PMODI1 - Etiquetado":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
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
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                    case "PROD - LINEA 04":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                switch (laborACT){
                                    case "PMODI1 - Lanzado":
                                    case "PMODI1 - Selección":
                                    case "PMODI1 - Clasificación":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                switch (laborACT){
                                    case "PMODI1 - Empaque":
                                    case "PMODI1 - Etiquetado":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
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
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "GENERAL":
                        switch (actividadACT){
                            case "ALMA - ALMACEN Y DESPACHO PPTT":
                                switch (laborACT){
                                    case "PMODI1 - Despacho":
                                    case "PMODI1 - Cámara":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "RECE - RECEPCION Y ACOPIO":
                                switch (laborACT){
                                    case "PMODI1 - Pesador":
                                    case "PMODI1 - Acopio":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "REEM - REEMPAQUE":
                                switch (laborACT){
                                    case "PMODI1 - Reempaque":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                }
                switch (procesoACT){
                    case "PROD - LINEA 01":
                        switch (actividadACT){
                            case "CLAS - SELECCIÓN Y CLASIFICACION":
                                switch (laborACT){
                                    case "PMODI1 - Lanzado":
                                    case "PMODI1 - Selección":
                                    case "PMODI1 - Clasificación":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "EMPP - EMPAQUE Y PALETIZADO":
                                switch (laborACT){
                                    case "PMODI1 - Empaque":
                                    case "PMODI1 - Etiquetado":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
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
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "GENERAL":
                        switch (actividadACT){
                            case "ALMA - ALMACEN Y DESPACHO PPTT":
                                switch (laborACT){
                                    case "PMODI1 - Despacho":
                                    case "PMODI1 - Cámara":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "RECE - RECEPCION Y ACOPIO":
                            case "REEM - REEMPAQUE":
                                switch (laborACT){
                                    case "PMODI1 - Pesador":
                                    case "PMODI1 - Acopio":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                }
                switch (procesoACT){
                    case "IND PRODUCCION":
                        switch (actividadACT){
                            case "AUXPRD - AUXILIAR DE PRODUCCION":
                                switch (laborACT){
                                    case "AUXPRD - Empaque":
                                    case "AUXPRD - Cámara":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "IND CALIDAD":
                        switch (actividadACT){
                            case "AUXCAL - AUXILIAR DE CALIDAD":
                                switch (laborACT){
                                    case "AUXCAL - Recepción":
                                    case "AUXCAL - Trazabilidad":
                                    case "AUXCAL - Paletizado":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "IND ACOPIO":
                        switch (actividadACT){
                            case "CHF001 - CHOFERES":
                                switch (laborACT){
                                    case "CHF001 - Choferes":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "AUXPRD - AUXILIAR DE PRODUCCION":
                                switch (laborACT){
                                    case "AUXPRD - Acopio":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "IND SANEAMIENTO":
                        switch (actividadACT){
                            case "AUXPRD - AUXILIAR DE PRODUCCION":
                                switch (laborACT){
                                    case "AUXPRD - Saneamiento":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "LIMPLT - LIMPIEZA PLANTA":
                                switch (laborACT){
                                    case "LIMPLT - Limpieza Planta":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "IND SOPORTE":
                        switch (actividadACT){
                            case "ALM001 - ALMACEN":
                                switch (laborACT){
                                    case "ALM001 - Almacén":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "MNT001 - MANTENIMIENTO":
                                switch (laborACT){
                                    case "MNT001 - Mantenimiento":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                            case "VIG001 - VIGILANTES":
                                switch (laborACT){
                                    case "VIG001 - Vigilantes":
                                        arrayMesas.clear();
                                        arrayMesas.add(new E_Mesas(mesa));
                                        arrayMesas.add(new E_Mesas("No Aplica"));
                                        break;
                                }
                                break;
                        }
                        break;
                }

                adaptadorMesas = new AdaptadorMesas(TercerNivelWelcomeTareoPlantaEdicion.this, arrayMesas);
                spMesa.setAdapter(adaptadorMesas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Mesas e_mesas = arrayMesas.get(position);
                mesaACT = e_mesas.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnActualizar.setOnClickListener(view -> actualizarDatos());
    }

    private void obtenerDatos(String grupo){
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL3 + " WHERE "
                + Utilidades.CAMPO_ANEXO_GRUPO_TAREO_PLANTA_NIVEL3 + "=" + "'"+grupo+"' ", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                proceso = cursor.getString(2);
                actividad = cursor.getString(3);
                labor = cursor.getString(4);
                mesa = cursor.getString(5);
            }
            cursor.close();

        }
    }
    private void obtenerLinea(String grupo){
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL1 + " WHERE "
                + Utilidades.CAMPO_IDNIVEL1_TAREO_PLANTA_NIVEL1 + "=" + "'"+grupo+"' ", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                lineaBD = cursor.getString(3);
            }
            cursor.close();
        }
    }

    public void llenarSpiners(){
        arrayActividades.add(new E_Actividades(actividad));
        arrayLabores.add(new E_Labores_Planta(labor));
        arrayMesas.add(new E_Mesas(mesa));

        adaptadorActividades = new AdaptadorActividades(TercerNivelWelcomeTareoPlantaEdicion.this, arrayActividades);
        spActividad.setAdapter(adaptadorActividades);
        adaptadorLaboresPlanta = new AdaptadorLaboresPlanta(TercerNivelWelcomeTareoPlantaEdicion.this, arrayLabores);
        spLabor.setAdapter(adaptadorLaboresPlanta);
        adaptadorMesas = new AdaptadorMesas(TercerNivelWelcomeTareoPlantaEdicion.this, arrayMesas);
        spMesa.setAdapter(adaptadorMesas);

        switch (lineaBD){
            case "FGR01 - GRANADA":
                arrayProcesos.clear();
                arrayProcesos.add(new E_Procesos(proceso));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 01"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 02"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 03"));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 04"));
                arrayProcesos.add(new E_Procesos("GENERAL"));

                adaptadorProcesos = new AdaptadorProcesos(TercerNivelWelcomeTareoPlantaEdicion.this, arrayProcesos);
                spProceso.setAdapter(adaptadorProcesos);
                break;
            case "FAR01 - ARANDANO":
                arrayProcesos.clear();
                arrayProcesos.add(new E_Procesos(proceso));
                arrayProcesos.add(new E_Procesos("PROD - LINEA 01"));
                arrayProcesos.add(new E_Procesos("GENERAL"));

                adaptadorProcesos = new AdaptadorProcesos(TercerNivelWelcomeTareoPlantaEdicion.this, arrayProcesos);
                spProceso.setAdapter(adaptadorProcesos);
                break;
            case "IND PRODUCCION":
                arrayProcesos.clear();
                arrayProcesos.add(new E_Procesos("IND PRODUCCION"));
                arrayProcesos.add(new E_Procesos("IND CALIDAD"));
                arrayProcesos.add(new E_Procesos("IND ACOPIO"));
                arrayProcesos.add(new E_Procesos("IND SANEAMIENTO"));
                arrayProcesos.add(new E_Procesos("IND SOPORTE"));

                adaptadorProcesos = new AdaptadorProcesos(TercerNivelWelcomeTareoPlantaEdicion.this, arrayProcesos);
                spProceso.setAdapter(adaptadorProcesos);
                break;
        }
    }

    private void actualizarDatos(){
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
        String[] parametro = {idGrupo};
        ContentValues valuesProceso = new ContentValues();

        valuesProceso.put(Utilidades.CAMPO_PROCESO_TAREO_PLANTA_NIVEL3, procesoACT);
        valuesProceso.put(Utilidades.CAMPO_ACTIVIDAD_TAREO_PLANTA_NIVEL3, actividadACT);
        valuesProceso.put(Utilidades.CAMPO_LABOR_TAREO_PLANTA_NIVEL3, laborACT);
        valuesProceso.put(Utilidades.CAMPO_MESA_TAREO_PLANTA_NIVEL3, mesaACT);

        database.update(Utilidades.TABLA_TAREO_PLANTA_NIVEL3, valuesProceso,
                Utilidades.CAMPO_ANEXO_GRUPO_TAREO_PLANTA_NIVEL3+"=?", parametro);
        Intent intent = new Intent(TercerNivelWelcomeTareoPlantaEdicion.this, SegundoNivelWelcomeTareoPlanta.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(TercerNivelWelcomeTareoPlantaEdicion.this, "Datos Actualizado!",
                Toast.LENGTH_SHORT).show();
    }
}