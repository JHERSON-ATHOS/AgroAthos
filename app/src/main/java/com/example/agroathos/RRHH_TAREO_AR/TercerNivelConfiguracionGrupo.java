package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.ENTIDADES.E_DetalleGrupoTrabajo;
import com.example.agroathos.ENTIDADES.E_Labores;
import com.example.agroathos.ENTIDADES.E_Lotes;
import com.example.agroathos.ENTIDADES.E_Modulos;
import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.R;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorLabores;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListaDetalleGrupoTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListarPersonalTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorLotes;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorModulos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class TercerNivelConfiguracionGrupo extends AppCompatActivity {

    ListView lvRegistrarPersonal;
    RecyclerView rvListadoPersonal;
    FloatingActionButton fbAgregarPersonal;
    Button btnEditarPersona, btnAgregarPersonal;
    ConstraintLayout clPrincipal, clConfigurarPrincipal;
    ArrayList<E_DetalleGrupoTrabajo> arrayPersonalList;

    /*CONFIGURACION*/
    TextView tvGrupo;
    Spinner spModulo, spLote, spLabor;
    EditText etHoraInicio, etHoraFinal;
    Button btnActualizarGrupo;
    RadioButton rbAbierto, rbCerrado;
    AdaptadorModulos adaptadorModulos;
    AdaptadorLotes adaptadorLotes;
    AdaptadorLabores adaptadorLabores;
    ArrayList<E_Modulos> arrayModulo = new ArrayList<>();
    ArrayList<E_Lotes> arrayLote = new ArrayList<>();
    ArrayList<E_Labores> arrayLabores = new ArrayList<>();
    String moduloACT = "";
    String loteACT = "";
    String laborACT = "";
    int hora = 0;
    int minutos = 0;

    ConexionSQLiteHelper conn;
    ContentValues valuesAgregarGrupo = new ContentValues();
    ContentValues valuesActualizarGrupo = new ContentValues();

    /*DATOS RECIBIDOS DEL ADAPTADOR*/
    String nombreGrupo = "";
    String idGrupo = "";
    String dni = "";
    int valor = 0;
    int validacion = 0;

    /*VALORES OBTENIDOS BD*/
    String BDid_nivel2 = "";
    String BDfundo = "";
    String BDmodulo = "";
    String BDlote = "";
    String BDlabor = "";
    String BDFecha = "";
    String BDhoraInicio = "";
    String BDhoraFinal = "";
    String BDestado = "";

    //AGREAR PERSONAL VARIABLES
    String valorPersonal = "";

    ArrayList<E_PersonalTrabajo> personalTrabajoArrayList = new ArrayList<>();
    AdaptadorListarPersonalTrabajo adaptadorListarPersonalTrabajo;
    int contador = 1;

    ArrayList<String> arrayPersonal = new ArrayList<>();
    SharedPreferences preferences;
    String idNivel1Capa1 = "";

    TextToSpeech voz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_configuracion_grupos);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        voz = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voz.setLanguage(new Locale("es", "pe"));
                }
            }
        });

        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        idNivel1Capa1 = preferences.getString("idNivel1","");

        Bundle bundle = getIntent().getExtras();
        nombreGrupo = bundle.getString("tvNombreGrupo");
        idGrupo = bundle.getString("idGrupo");
        dni = bundle.getString("dni");
        valor = bundle.getInt("valor");
        validacion = bundle.getInt("validacion");

        consultarGruposTrabajo();

        if (valor == 1){
            iniciarLayoutPrincipal();
            clPrincipal.setVisibility(View.VISIBLE);
            btnEditarPersona.setVisibility(View.VISIBLE);

            if (validacion == 1){
                fbAgregarPersonal.setEnabled(false);
            }

        }

        if (valor == 2){
            iniciarLayoutPrincipal();
            duplicarGrupoTrabajo();
        }

        if (valor == 3){
            iniciarLayoutConfiguracion();
            clConfigurarPrincipal.setVisibility(View.VISIBLE);
        }

    }

    public void iniciarLayoutPrincipal(){
        clPrincipal = findViewById(R.id.clPrincipalRRHH_TAREO_AR);
        rvListadoPersonal = findViewById(R.id.rvListadoPersonalACTTercerlNivelRRHH_TAREO_ARANDANO);
        lvRegistrarPersonal = findViewById(R.id.lvListadoPersonalRegTercerlNivelRRHH_TAREO_ARANDANO);
        fbAgregarPersonal = findViewById(R.id.fbAgregarPersonalNuevoRRHH_TAREO_AR);
        btnEditarPersona = findViewById(R.id.btnEditarPersonalNuevoRRHH_TAREO_AR);
        btnAgregarPersonal = findViewById(R.id.btnRegistrarPersonalNuevoRRHH_TAREO_AR);

        rvListadoPersonal.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorListaDetalleGrupoTrabajo adaptadorListaDetalleGrupoTrabajo = new AdaptadorListaDetalleGrupoTrabajo(this,arrayPersonalList);
        rvListadoPersonal.setAdapter(adaptadorListaDetalleGrupoTrabajo);

        fbAgregarPersonal.setOnClickListener(view->{
            rvListadoPersonal.setVisibility(View.GONE);
            lvRegistrarPersonal.setVisibility(View.VISIBLE);
            iniciarScanPersonal();
        });
        btnEditarPersona.setOnClickListener(view->registrarNuevoPersonal());
        //btnAgregarPersonal.setOnClickListener(view->registrarNuevoPersonal());
    }

    public void iniciarLayoutConfiguracion(){
        clConfigurarPrincipal = findViewById(R.id.clConfiguracionPrincipalRRHH_TAREO_AR);
        tvGrupo = findViewById(R.id.tvGrupoActTERCER_NIVEL_RRHH_TAREO_AR);
        spModulo = findViewById(R.id.spModuloActTERCER_NIVEL_RRHH_TAREO_AR);
        spLote = findViewById(R.id.spLoteActTERCER_NIVEL_RRHH_TAREO_AR);
        spLabor = findViewById(R.id.spLaborActTERCER_NIVEL_RRHH_TAREO_AR);
        etHoraInicio = findViewById(R.id.ethoraInicioActTERCER_NIVEL_RRHH_TAREO_AR);
        etHoraFinal = findViewById(R.id.etHoraFinalActTERCER_NIVEL_RRHH_TAREO_AR);
        btnActualizarGrupo = findViewById(R.id.btnActualizarGrupoTrabajoTERCER_NIVEL_RRHH_TAREO_AR);
        rbAbierto = findViewById(R.id.rbEstadoAbiertoActTERCER_NIVEL_RRHH_TAREO_AR);
        rbCerrado = findViewById(R.id.rbEstadoCerradoActTERCER_NIVEL_RRHH_TAREO_AR);

        tvGrupo.setText(nombreGrupo);

        recibirFundo();
        cargarModulo();
        cargarLabores();

        if (BDestado.equals("ABIERTO")){
            rbAbierto.setChecked(true);
        }else if (BDestado.equals("CERRADO")){
            rbCerrado.setChecked(true);
        }

        etHoraInicio.setText(BDhoraInicio);
        etHoraFinal.setText(BDhoraFinal);

        etHoraInicio.setOnClickListener(view ->{
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    etHoraInicio.setText(hourOfDay+":"+minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        });
        etHoraFinal.setOnClickListener(view ->{
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    etHoraFinal.setText(hourOfDay+":"+minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        });

        spModulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                E_Modulos e_modulos = arrayModulo.get(i);
                moduloACT = e_modulos.getNombre();

                switch (moduloACT){
                    case"MO1":
                        switch (BDfundo){
                            case "CAY":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L11"));
                                arrayLote.add(new E_Lotes("L12"));
                                arrayLote.add(new E_Lotes("L13"));
                                arrayLote.add(new E_Lotes("L14"));
                                arrayLote.add(new E_Lotes("L15"));
                                arrayLote.add(new E_Lotes("L16"));
                                arrayLote.add(new E_Lotes("L17"));
                                break;
                            case "CHI":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L2A"));
                                arrayLote.add(new E_Lotes("L3A"));
                                arrayLote.add(new E_Lotes("L5A"));
                                arrayLote.add(new E_Lotes("LTN"));
                                break;
                            case "LDN":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                break;
                            case "LIN":
                            case "MEN":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                break;
                            case "LPO":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("LRJ"));
                                arrayLote.add(new E_Lotes("LTQ"));
                                break;
                            case "LU1":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L04"));
                                break;
                            case "LUC":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L4A"));
                                break;
                            case "MAC":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L4A"));
                                arrayLote.add(new E_Lotes("LD1"));
                                arrayLote.add(new E_Lotes("LD2"));
                                break;
                            case "MAT":
                            case "SOJ":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                break;
                            case "PAR":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("LTI"));
                                break;
                            case "POM":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                break;
                            case "SAT":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("LTA"));
                                arrayLote.add(new E_Lotes("LTB"));
                                arrayLote.add(new E_Lotes("LTC"));
                                arrayLote.add(new E_Lotes("LTD"));
                                break;
                            case "SCA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L11"));
                                arrayLote.add(new E_Lotes("L7A"));
                                arrayLote.add(new E_Lotes("L9A"));
                                arrayLote.add(new E_Lotes("L11A"));
                                break;
                            case "SLA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L11"));
                                break;
                            case "SNA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("LE1"));
                                arrayLote.add(new E_Lotes("LE2"));
                                arrayLote.add(new E_Lotes("LTI"));
                                break;
                            case "SOI":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L4A"));
                                arrayLote.add(new E_Lotes("LD1"));
                                break;
                            case "STF":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("LD1"));
                                arrayLote.add(new E_Lotes("LD2"));
                                break;
                        }
                        break;
                    case"MO2":
                        switch (BDfundo){
                            case "CHI":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L2A"));
                                arrayLote.add(new E_Lotes("L3A"));
                                arrayLote.add(new E_Lotes("L4A"));
                                break;
                            case "LDN":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L11"));
                                arrayLote.add(new E_Lotes("L12"));
                                arrayLote.add(new E_Lotes("L13"));
                                arrayLote.add(new E_Lotes("L14"));
                                arrayLote.add(new E_Lotes("L15"));
                                break;
                            case "LU2":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                break;
                            case "LU3":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                break;
                            case "LUC":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                break;
                            case "MAC":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("LRE"));
                                break;
                            case "PAR":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L06"));
                                break;
                            case "POM":
                            case "SOI":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                break;
                            case "SAT":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("LTI"));
                                arrayLote.add(new E_Lotes("LTJ"));
                                arrayLote.add(new E_Lotes("LTK"));
                                arrayLote.add(new E_Lotes("LTL"));
                                break;
                            case "SCA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L11"));
                                arrayLote.add(new E_Lotes("L4A"));
                                arrayLote.add(new E_Lotes("L5A"));
                                arrayLote.add(new E_Lotes("L7A"));
                                break;
                            case "SLA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                arrayLote.add(new E_Lotes("L05"));
                                arrayLote.add(new E_Lotes("L06"));
                                arrayLote.add(new E_Lotes("L07"));
                                arrayLote.add(new E_Lotes("L08"));
                                arrayLote.add(new E_Lotes("L09"));
                                arrayLote.add(new E_Lotes("L10"));
                                arrayLote.add(new E_Lotes("L11"));
                                break;
                            case "SNA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("LTD"));
                                arrayLote.add(new E_Lotes("LTF"));
                                arrayLote.add(new E_Lotes("LTG"));
                                arrayLote.add(new E_Lotes("LTH"));
                                break;
                        }
                        break;
                    case "MO3":
                        switch (BDfundo){
                            case "POM":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("L01"));
                                arrayLote.add(new E_Lotes("L02"));
                                arrayLote.add(new E_Lotes("L03"));
                                arrayLote.add(new E_Lotes("L04"));
                                break;
                            case "SNA":
                                arrayLote.clear();
                                arrayLote.add(new E_Lotes("LJ7"));
                                arrayLote.add(new E_Lotes("LJ8"));
                                break;
                        }
                        break;
                    case "MO4":
                    case "MO6":
                        if (BDfundo.equals("POM")){
                            arrayLote.clear();
                            arrayLote.add(new E_Lotes("L01"));
                            arrayLote.add(new E_Lotes("L02"));
                            arrayLote.add(new E_Lotes("L03"));
                            arrayLote.add(new E_Lotes("L04"));
                        }
                        break;
                    case "MO5":
                        if (BDfundo.equals("POM")){
                            arrayLote.clear();
                            arrayLote.add(new E_Lotes("L01"));
                            arrayLote.add(new E_Lotes("L02"));
                            arrayLote.add(new E_Lotes("L03"));
                            arrayLote.add(new E_Lotes("L04"));
                            arrayLote.add(new E_Lotes("L05"));
                        }
                        break;
                }

                adaptadorLotes = new AdaptadorLotes(TercerNivelConfiguracionGrupo.this, arrayLote);
                spLote.setAdapter(adaptadorLotes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spLote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                E_Lotes e_lotes = arrayLote.get(i);
                loteACT = e_lotes.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spLabor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                E_Labores e_labores = arrayLabores.get(i);
                laborACT = e_labores.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnActualizarGrupo.setOnClickListener(view -> actualizarGrupoTrabajo());
    }

    public void recibirFundo(){
        arrayModulo.add(new E_Modulos(BDmodulo));
        arrayLote.add(new E_Lotes(BDlote));
        arrayLabores.add(new E_Labores(BDlabor));
        switch (BDfundo){
            case "CAY":
            case "STF":
            case "LIN":
            case "MEN":
            case "LPO":
            case "LU1":
            case "MAT":
            case "SOJ":
                arrayModulo.clear();
                arrayModulo.add(new E_Modulos(BDmodulo));
                arrayModulo.add(new E_Modulos("MO1"));
                break;
            case "CHI":
            case "SOI":
            case "SLA":
            case "SCA":
            case "SAT":
            case "PAR":
            case "MAC":
            case "LUC":
            case "LDN":
                arrayModulo.clear();
                arrayModulo.add(new E_Modulos(BDmodulo));
                arrayModulo.add(new E_Modulos("MO1"));
                arrayModulo.add(new E_Modulos("MO2"));
                break;
            case "LU2":
            case "LU3":
                arrayModulo.clear();
                arrayModulo.add(new E_Modulos(BDmodulo));
                arrayModulo.add(new E_Modulos("MO2"));
                break;
            case "POM":
                arrayModulo.clear();
                arrayModulo.add(new E_Modulos(BDmodulo));
                arrayModulo.add(new E_Modulos("MO1"));
                arrayModulo.add(new E_Modulos("MO2"));
                arrayModulo.add(new E_Modulos("MO3"));
                arrayModulo.add(new E_Modulos("MO4"));
                arrayModulo.add(new E_Modulos("MO5"));
                arrayModulo.add(new E_Modulos("MO6"));
                break;
            case "SNA":
                arrayModulo.clear();
                arrayModulo.add(new E_Modulos(BDmodulo));
                arrayModulo.add(new E_Modulos("MO1"));
                arrayModulo.add(new E_Modulos("MO2"));
                arrayModulo.add(new E_Modulos("MO3"));
                break;
        }
    }
    public void cargarModulo(){
        adaptadorModulos = new AdaptadorModulos(TercerNivelConfiguracionGrupo.this, arrayModulo);
        spModulo.setAdapter(adaptadorModulos);
    }
    public void cargarLabores(){
        arrayLabores.add(new E_Labores("7001 - COSECHA"));
        arrayLabores.add(new E_Labores("7002 - ESTIBADORES"));
        arrayLabores.add(new E_Labores("7010 - TRANSPORTE DE MATERIALES"));
        arrayLabores.add(new E_Labores("7012 - ACOPIO"));
        arrayLabores.add(new E_Labores("7017 - JABEROS"));
        arrayLabores.add(new E_Labores("7018 - SUPERVISOR DE COSECHA"));
        arrayLabores.add(new E_Labores("7019 - CONTROL DE CALIDAD"));
        adaptadorLabores = new AdaptadorLabores(TercerNivelConfiguracionGrupo.this, arrayLabores);
        spLabor.setAdapter(adaptadorLabores);
    }

    private void actualizarGrupoTrabajo(){

        if (etHoraInicio.getText().toString().equals("") || etHoraFinal.getText().toString().equals("")){
            Toast.makeText(this, "Ingresa una hora", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();
        String[] parametro = {idGrupo};

        if (rbAbierto.isChecked()){
            valuesActualizarGrupo.put(Utilidades.CAMPO_MODULO_NIVEL2, moduloACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LOTE_NIVEL2, loteACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LABOR_NIVEL2, laborACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, etHoraInicio.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");
            valuesActualizarGrupo.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL2, "0");
        }
        if (rbCerrado.isChecked()){
            valuesActualizarGrupo.put(Utilidades.CAMPO_MODULO_NIVEL2, moduloACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LOTE_NIVEL2, loteACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LABOR_NIVEL2, laborACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, etHoraInicio.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_ESTADO_NIVEL2, "CERRADO");
            valuesActualizarGrupo.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL2, "0");
        }

        database.update(Utilidades.TABLA_NIVEL2, valuesActualizarGrupo, Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2+"=?", parametro);
        Intent intent = new Intent(TercerNivelConfiguracionGrupo.this, SegundoNivelWelcome.class);
        startActivity(intent);
        Toast.makeText(TercerNivelConfiguracionGrupo.this, "Grupo Actualizado!", Toast.LENGTH_SHORT).show();
    }

    private void consultarGruposTrabajo(){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2 + "=" + "'"+idGrupo+"'", null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    E_DetalleGrupoTrabajo e_detalleGrupoTrabajo = new E_DetalleGrupoTrabajo();
                    e_detalleGrupoTrabajo.setId(cursor.getString(0));
                    e_detalleGrupoTrabajo.setId_grupo(cursor.getString(1));
                    e_detalleGrupoTrabajo.setFundo(cursor.getString(2));
                    e_detalleGrupoTrabajo.setModulo(cursor.getString(3));
                    e_detalleGrupoTrabajo.setLote(cursor.getString(4));
                    e_detalleGrupoTrabajo.setLabor(cursor.getString(5));
                    e_detalleGrupoTrabajo.setPersonal(cursor.getString(6));
                    e_detalleGrupoTrabajo.setSupervisor(cursor.getString(7));
                    e_detalleGrupoTrabajo.setFecha(cursor.getString(8));
                    e_detalleGrupoTrabajo.setHora_inicio(cursor.getString(9));
                    e_detalleGrupoTrabajo.setHora_final(cursor.getString(10));
                    e_detalleGrupoTrabajo.setEstado(cursor.getString(11));
                    arrayPersonalList.add(e_detalleGrupoTrabajo);

                    BDid_nivel2 = cursor.getString(0);
                    BDfundo = cursor.getString(2);
                    BDmodulo = cursor.getString(3);
                    BDlote = cursor.getString(4);
                    BDlabor = cursor.getString(5);
                    BDFecha = cursor.getString(8);
                    BDhoraInicio = cursor.getString(9);
                    BDhoraFinal = cursor.getString(10);
                    BDestado = cursor.getString(11);
                }while (cursor.moveToNext());//
            }
        }
    }

    private void registrarNuevoPersonal(){
        if (personalTrabajoArrayList.size()>0){
            SQLiteDatabase database = conn.getWritableDatabase();
            ContentValues valuesPersonal = new ContentValues();
            for (int i=0; i<lvRegistrarPersonal.getCount(); i++){
                valuesPersonal.put(Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2, idGrupo);
                valuesPersonal.put(Utilidades.CAMPO_FUNDO_NIVEL2, BDfundo);
                valuesPersonal.put(Utilidades.CAMPO_MODULO_NIVEL2, BDmodulo);
                valuesPersonal.put(Utilidades.CAMPO_LOTE_NIVEL2, BDlote);
                valuesPersonal.put(Utilidades.CAMPO_LABOR_NIVEL2, BDlabor);
                valuesPersonal.put(Utilidades.CAMPO_PERSONAL_NIVEL2, arrayPersonal.get(i));
                valuesPersonal.put(Utilidades.CAMPO_DNI_NIVEL2, dni);
                valuesPersonal.put(Utilidades.CAMPO_FECHA_NIVEL2, BDFecha);
                valuesPersonal.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, BDhoraInicio);
                valuesPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, BDhoraFinal);
                valuesPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");
                valuesPersonal.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL2, "0");

                Long idResultante = database.insert(Utilidades.TABLA_NIVEL2, Utilidades.CAMPO_ID_NIVEL2, valuesPersonal);

                if (idResultante > 0){
                    Toast.makeText(this, "Datos Registrados!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(TercerNivelConfiguracionGrupo.this, SegundoNivelWelcome.class);
                    startActivity(intent);
                }
            }
        }else{
            Intent intent = new Intent(TercerNivelConfiguracionGrupo.this, SegundoNivelWelcome.class);
            startActivity(intent);
        }
    }

    private void duplicarGrupoTrabajo(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorPersonal = dataObtenida.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2 + "=" + "'"+idGrupo+"' AND "+Utilidades.CAMPO_ESTADO_NIVEL2+ "=" + "'ABIERTO'", null);

        ArrayList<String> arrayListFundos = new ArrayList<>();
        ArrayList<String> arrayListModulos = new ArrayList<>();
        ArrayList<String> arrayListLotes = new ArrayList<>();
        ArrayList<String> arrayListLabores = new ArrayList<>();
        ArrayList<String> arrayListPersonales = new ArrayList<>();
        ArrayList<String> arrayListSupervisores = new ArrayList<>();
        ArrayList<String> arrayListFechas = new ArrayList<>();
        ArrayList<String> arrayListHorasInicio = new ArrayList<>();
        ArrayList<String> arrayListHorasFinal = new ArrayList<>();
        ArrayList<String> arrayListEstados = new ArrayList<>();
        ArrayList<String> arrayListSinc = new ArrayList<>();

        if (cursorPersonal != null){
            if (cursorPersonal.moveToFirst()){
                do{
                    arrayListFundos.add(cursorPersonal.getString(2));
                    arrayListModulos.add(cursorPersonal.getString(3));
                    arrayListLotes.add(cursorPersonal.getString(4));
                    arrayListLabores.add(cursorPersonal.getString(5));
                    arrayListPersonales.add(cursorPersonal.getString(6));
                    arrayListSupervisores.add(cursorPersonal.getString(7));
                    arrayListFechas.add(cursorPersonal.getString(8));
                    arrayListHorasInicio.add(cursorPersonal.getString(9));
                    arrayListHorasFinal.add(cursorPersonal.getString(10));
                    arrayListEstados.add(cursorPersonal.getString(11));
                    arrayListSinc.add("0");
                }while (cursorPersonal.moveToNext());
            }
        }

        SQLiteDatabase databaseGrupo = conn.getWritableDatabase();
        Cursor cursorGrupo = databaseGrupo.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL1_5 + " WHERE " + Utilidades.CAMPO_DNI_NIVEL1_5 + "=" + "'"+dni+"'", null);

        String idNewGrupo = UUID.randomUUID().toString();
        String idAnexoGrupo1 = UUID.randomUUID().toString();

        if (cursorGrupo.getCount()<0){
            valuesAgregarGrupo.put(Utilidades.CAMPO_ANEXONIVEL1_NIVEL1_5, idAnexoGrupo1);
            valuesAgregarGrupo.put(Utilidades.CAMPO_ID_GRUPO_NIVEL1_5, idNewGrupo);
            valuesAgregarGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_NIVEL1_5, 1);
            valuesAgregarGrupo.put(Utilidades.CAMPO_DNI_NIVEL1_5, dni);
            valuesAgregarGrupo.put(Utilidades.CAMPO_ESTADO_NIVEL1_5, "ABIERTO");
            valuesAgregarGrupo.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL1_5, "0");
        }else{
            valuesAgregarGrupo.put(Utilidades.CAMPO_ANEXONIVEL1_NIVEL1_5, idNivel1Capa1);
            valuesAgregarGrupo.put(Utilidades.CAMPO_ID_GRUPO_NIVEL1_5, idNewGrupo);
            valuesAgregarGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_NIVEL1_5, cursorGrupo.getCount()+1);
            valuesAgregarGrupo.put(Utilidades.CAMPO_DNI_NIVEL1_5, dni);
            valuesAgregarGrupo.put(Utilidades.CAMPO_ESTADO_NIVEL1_5, "ABIERTO");
            valuesAgregarGrupo.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL1_5, "0");
        }

        databaseGrupo.insert(Utilidades.TABLA_NIVEL1_5, Utilidades.CAMPO_ID_NIVEL1_5, valuesAgregarGrupo);

        for (int i=0; i<arrayListSupervisores.size(); i++){
            SQLiteDatabase database = conn.getWritableDatabase();
            ContentValues valuesPersonalClonado = new ContentValues();

            valuesPersonalClonado.put(Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2, idNewGrupo);
            valuesPersonalClonado.put(Utilidades.CAMPO_FUNDO_NIVEL2, arrayListFundos.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_MODULO_NIVEL2, arrayListModulos.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_LOTE_NIVEL2, arrayListLotes.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_LABOR_NIVEL2, arrayListLabores.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_PERSONAL_NIVEL2, arrayListPersonales.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_DNI_NIVEL2, arrayListSupervisores.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_FECHA_NIVEL2, arrayListFechas.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, arrayListHorasInicio.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, arrayListHorasFinal.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_ESTADO_NIVEL2, arrayListEstados.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL2, arrayListSinc.add("0"));

            Long idResultante = database.insert(Utilidades.TABLA_NIVEL2, Utilidades.CAMPO_ID_NIVEL2, valuesPersonalClonado);

            if (idResultante > 0){
                Toast.makeText(this, "Grupo Clonado!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TercerNivelConfiguracionGrupo.this, SegundoNivelWelcome.class);
                startActivity(intent);
            }
        }
    }

    private void iniciarScanPersonal(){
        IntentIntegrator integrator = new IntentIntegrator(TercerNivelConfiguracionGrupo.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("LECTOR QR PERSONAL");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void verificarSincronizados(String idGrupo){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1_5 + "=" + "'1'", null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {

                }while (cursor.moveToNext());
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            String scanFormat = intentResult.getFormatName();

            if (!TextUtils.isEmpty(scanFormat)) {
                if (scanFormat.equals("QR_CODE") || scanFormat.equals("CODE_39")) {
                    if (intentResult.getContents() == null){

                        if (valorPersonal.isEmpty()){
                            personalTrabajoArrayList.add(new E_PersonalTrabajo(String.valueOf(contador++), valorPersonal));
                            iniciarScanPersonal();
                            valorPersonal = "";
                            Toast.makeText(this, "Listando los registros", Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("ERROR");
                            builder.setMessage("Te faltaron llenar datos.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    valorPersonal = "";
                                    arrayPersonal.clear();
                                    iniciarScanPersonal();
                                }
                            });
                            builder.create().show();
                        }
                        adaptadorListarPersonalTrabajo = new AdaptadorListarPersonalTrabajo(this, personalTrabajoArrayList);
                        lvRegistrarPersonal.setAdapter(adaptadorListarPersonalTrabajo);
                    }else{
                        if (valorPersonal.isEmpty()){
                            if (arrayPersonal.contains(intentResult.getContents())){
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("ERROR");
                                builder.setMessage("El personal ya se encuentra en la lista.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        iniciarScanPersonal();
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
                                        iniciarScanPersonal();
                                    }else{
                                        voz.speak("REGISTRADO", TextToSpeech.QUEUE_FLUSH, bundle, null);
                                        arrayPersonal.add(intentResult.getContents());
                                        valorPersonal = intentResult.getContents();
                                        personalTrabajoArrayList.add(new E_PersonalTrabajo(String.valueOf(contador++), valorPersonal));
                                        valorPersonal = "";
                                        iniciarScanPersonal();
                                    }

                                }catch (Exception e){
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }else{
                adaptadorListarPersonalTrabajo = new AdaptadorListarPersonalTrabajo(this, personalTrabajoArrayList);
                lvRegistrarPersonal.setAdapter(adaptadorListarPersonalTrabajo);
            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TercerNivelConfiguracionGrupo.this, SegundoNivelWelcome.class);
        startActivity(intent);
    }
}