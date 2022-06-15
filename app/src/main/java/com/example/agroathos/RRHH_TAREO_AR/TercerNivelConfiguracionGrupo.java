package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.R;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListaDetalleGrupoTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListarPersonalTrabajo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
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
    ArrayList<String> arrayModulo = new ArrayList<>();
    ArrayList<String> arrayLote = new ArrayList<>();
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

    /*VALORES OBTENIDOS BD*/
    String BDid_nivel2 = "";
    String BDgrupo = "";
    String BDfundo = "";
    String BDmodulo = "";
    String BDlote = "";
    String BDlabor = "";
    String BDdniPersonal = "";
    String BDdniSupervisor = "";
    String BDFecha = "";
    String BDhoraInicio = "";
    String BDhoraFinal = "";
    String BDestado = "";

    //AGREAR PERSONAL VARIABLES
    int contadorJarras = 0;
    String valorPersonal = "";
    String valorJarra1 = "";
    String valorJarra2 = "";

    ArrayList<E_PersonalTrabajo> personalTrabajoArrayList = new ArrayList<>();
    AdaptadorListarPersonalTrabajo adaptadorListarPersonalTrabajo;
    int contador = 1;

    ArrayList<String> arrayPersonal = new ArrayList<>();
    ArrayList<String> arrayJarras1 = new ArrayList<>();
    ArrayList<String> arrayJarras2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_configuracion_grupos);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        Bundle bundle = getIntent().getExtras();
        nombreGrupo = bundle.getString("tvNombreGrupo");
        idGrupo = bundle.getString("idGrupo");
        dni = bundle.getString("dni");
        valor = bundle.getInt("valor");

        consultarGruposTrabajo();

        if (valor == 1){
            iniciarLayoutPrincipal();
            clPrincipal.setVisibility(View.VISIBLE);
            btnEditarPersona.setVisibility(View.VISIBLE);
        }

        if (valor == 2){
            iniciarLayoutPrincipal();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);

            final View view = getLayoutInflater().inflate(R.layout.load_transporte_garita_clonacion_grupos, null, false);
            ProgressBar progressBar = view.findViewById(R.id.progressBarClonandoGrupoTRANSPORTE_GARITA);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    duplicarGrupoTrabajo();
                }
            }, 5000);

            builder.setView(view);
            builder.create().show();

            btnAgregarPersonal.setVisibility(View.VISIBLE);
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
                arrayLote.clear();
                moduloACT = spModulo.getItemAtPosition(i).toString();

                switch (moduloACT){
                    case"MO1":
                        switch (BDfundo){
                            case "CAY":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L11");
                                arrayLote.add("L12");
                                arrayLote.add("L13");
                                arrayLote.add("L14");
                                arrayLote.add("L15");
                                arrayLote.add("L16");
                                arrayLote.add("L17");
                                break;
                            case "CHI":
                                arrayLote.add("L01");
                                arrayLote.add("L03");
                                arrayLote.add("L05");
                                arrayLote.add("L2A");
                                arrayLote.add("L3A");
                                arrayLote.add("L5A");
                                arrayLote.add("LTN");
                                break;
                            case "LDN":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                break;
                            case "LIN":
                            case "MEN":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                break;
                            case "LPO":
                                arrayLote.add("LRJ");
                                arrayLote.add("LTQ");
                                break;
                            case "LU1":
                                arrayLote.add("L01");
                                arrayLote.add("L04");
                                break;
                            case "LUC":
                                arrayLote.add("L04");
                                arrayLote.add("L4A");
                                break;
                            case "MAC":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L4A");
                                arrayLote.add("LD1");
                                arrayLote.add("LD2");
                                break;
                            case "MAT":
                            case "SOJ":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                break;
                            case "PAR":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L07");
                                arrayLote.add("LTI");
                                break;
                            case "POM":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                break;
                            case "SAT":
                                arrayLote.add("LTA");
                                arrayLote.add("LTB");
                                arrayLote.add("LTC");
                                arrayLote.add("LTD");
                                break;
                            case "SCA":
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L11");
                                arrayLote.add("L7A");
                                arrayLote.add("L9A");
                                arrayLote.add("L11A");
                                break;
                            case "SLA":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L11");
                                break;
                            case "SNA":
                                arrayLote.add("L01");
                                arrayLote.add("L07");
                                arrayLote.add("LE1");
                                arrayLote.add("LE2");
                                arrayLote.add("LTI");
                                break;
                            case "SOI":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L4A");
                                arrayLote.add("LD1");
                                break;
                            case "STF":
                                arrayLote.add("L02");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("LD1");
                                arrayLote.add("LD2");
                                break;
                        }
                        break;
                    case"MO2":
                        switch (BDfundo){
                            case "CHI":
                                arrayLote.add("L02");
                                arrayLote.add("L04");
                                arrayLote.add("L06");
                                arrayLote.add("L08");
                                arrayLote.add("L2A");
                                arrayLote.add("L3A");
                                arrayLote.add("L4A");
                                break;
                            case "LDN":
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L11");
                                arrayLote.add("L12");
                                arrayLote.add("L13");
                                arrayLote.add("L14");
                                arrayLote.add("L15");
                                break;
                            case "LU2":
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                break;
                            case "LU3":
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                break;
                            case "LUC":
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                break;
                            case "MAC":
                                arrayLote.add("LRE");
                                break;
                            case "PAR":
                                arrayLote.add("L06");
                                break;
                            case "POM":
                            case "SOI":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                break;
                            case "SAT":
                                arrayLote.add("LTI");
                                arrayLote.add("LTJ");
                                arrayLote.add("LTK");
                                arrayLote.add("LTL");
                                break;
                            case "SCA":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L11");
                                arrayLote.add("L4A");
                                arrayLote.add("L5A");
                                arrayLote.add("L7A");
                                break;
                            case "SLA":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                arrayLote.add("L05");
                                arrayLote.add("L06");
                                arrayLote.add("L07");
                                arrayLote.add("L08");
                                arrayLote.add("L09");
                                arrayLote.add("L10");
                                arrayLote.add("L11");
                                break;
                            case "SNA":
                                arrayLote.add("LTD");
                                arrayLote.add("LTF");
                                arrayLote.add("LTG");
                                arrayLote.add("LTH");
                                break;
                        }
                        break;
                    case "MO3":
                        switch (BDfundo){
                            case "POM":
                                arrayLote.add("L01");
                                arrayLote.add("L02");
                                arrayLote.add("L03");
                                arrayLote.add("L04");
                                break;
                            case "SNA":
                                arrayLote.add("LJ7");
                                arrayLote.add("LJ8");
                                break;
                        }
                        break;
                    case "MO4":
                    case "MO6":
                        if (BDfundo.equals("POM")){
                            arrayLote.add("L01");
                            arrayLote.add("L02");
                            arrayLote.add("L03");
                            arrayLote.add("L04");
                        }
                        break;
                    case "MO5":
                        if (BDfundo.equals("POM")){
                            arrayLote.add("L01");
                            arrayLote.add("L02");
                            arrayLote.add("L03");
                            arrayLote.add("L04");
                            arrayLote.add("L05");
                        }
                        break;
                }

                ArrayAdapter<String> adaptadorLote = new ArrayAdapter<>(TercerNivelConfiguracionGrupo.this, android.R.layout.simple_spinner_dropdown_item, arrayLote);
                spLote.setAdapter(adaptadorLote);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spLote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loteACT = spLote.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spLabor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                laborACT = spLabor.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnActualizarGrupo.setOnClickListener(view -> actualizarGrupoTrabajo());
    }

    public void recibirFundo(){
        switch (BDfundo){
            case "CAY":
            case "STF":
            case "LIN":
            case "MEN":
            case "LPO":
            case "LU1":
            case "MAT":
            case "SOJ":
                arrayModulo.add("MO1");
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
                arrayModulo.add("MO1");
                arrayModulo.add("MO2");
                break;
            case "LU2":
            case "LU3":
                arrayModulo.add("MO2");
                break;
            case "POM":
                arrayModulo.add("MO1");
                arrayModulo.add("MO2");
                arrayModulo.add("MO3");
                arrayModulo.add("MO4");
                arrayModulo.add("MO5");
                arrayModulo.add("MO6");
                break;
            case "SNA":
                arrayModulo.add("MO1");
                arrayModulo.add("MO2");
                arrayModulo.add("MO3");
                break;
        }
    }
    public void cargarModulo(){
        ArrayAdapter<String> adaptadorModulo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayModulo);
        spModulo.setAdapter(adaptadorModulo);
    }
    public void cargarLabores(){
        String[] labores = {"ARREGLO DE CABECERAS/SURCOS","ARREGLO DE PLANTAS","COSECHA"};
        ArrayAdapter<String> adaptadorLabores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labores);
        spLabor.setAdapter(adaptadorLabores);
    }
    private void actualizarGrupoTrabajo(){

        if (etHoraInicio.getText().toString().equals("") || etHoraFinal.getText().toString().equals("")){
            Toast.makeText(this, "Ingresa una hora", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();
        String[] parametro = {BDid_nivel2};

        if (rbAbierto.isChecked()){
            valuesActualizarGrupo.put(Utilidades.CAMPO_MODULO_NIVEL2, moduloACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LOTE_NIVEL2, loteACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LABOR_NIVEL2, laborACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, etHoraInicio.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");
        }else if (rbCerrado.isChecked()){
            valuesActualizarGrupo.put(Utilidades.CAMPO_MODULO_NIVEL2, moduloACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LOTE_NIVEL2, loteACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_LABOR_NIVEL2, laborACT);
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, etHoraInicio.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
            valuesActualizarGrupo.put(Utilidades.CAMPO_ESTADO_NIVEL2, "CERRADO");
        }

        database.update(Utilidades.TABLA_NIVEL2, valuesActualizarGrupo, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);
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
                    e_detalleGrupoTrabajo.setJarra_uno(cursor.getString(8));
                    e_detalleGrupoTrabajo.setJarra_dos(cursor.getString(9));
                    e_detalleGrupoTrabajo.setFecha(cursor.getString(10));
                    e_detalleGrupoTrabajo.setHora_inicio(cursor.getString(11));
                    e_detalleGrupoTrabajo.setHora_final(cursor.getString(12));
                    e_detalleGrupoTrabajo.setEstado(cursor.getString(13));
                    arrayPersonalList.add(e_detalleGrupoTrabajo);

                    BDhoraInicio = cursor.getString(11);
                    BDhoraFinal = cursor.getString(12);
                    BDestado = cursor.getString(13);
                }while (cursor.moveToNext());//
            }
            /*while (cursor.moveToNext()) {
                arrayPersonalList.add(cursor.getString(6));
                BDid_nivel2 = cursor.getString(0);
                BDgrupo = cursor.getString(1);
                BDfundo = cursor.getString(2);
                BDmodulo = cursor.getString(3);
                BDlote = cursor.getString(4);
                BDlabor = cursor.getString(5);
                BDdniPersonal = cursor.getString(6);
                BDdniSupervisor = cursor.getString(7);
                BDFecha = cursor.getString(10);
                BDhoraInicio = cursor.getString(11);
                BDhoraFinal = cursor.getString(12);
                BDestado = cursor.getString(13);
            }*/
        }
    }

    private void registrarNuevoPersonal(){
        if (personalTrabajoArrayList.size()>0){
            SQLiteDatabase database = conn.getWritableDatabase();
            ContentValues valuesPersonal = new ContentValues();
            for (int i=0; i<rvListadoPersonal.getChildCount(); i++){
                valuesPersonal.put(Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2, idGrupo);
                valuesPersonal.put(Utilidades.CAMPO_FUNDO_NIVEL2, BDfundo);
                valuesPersonal.put(Utilidades.CAMPO_MODULO_NIVEL2, BDmodulo);
                valuesPersonal.put(Utilidades.CAMPO_LOTE_NIVEL2, BDlote);
                valuesPersonal.put(Utilidades.CAMPO_LABOR_NIVEL2, BDlabor);
                valuesPersonal.put(Utilidades.CAMPO_PERSONAL_NIVEL2, arrayPersonal.get(i));
                valuesPersonal.put(Utilidades.CAMPO_DNI_NIVEL2, dni);
                valuesPersonal.put(Utilidades.CAMPO_JARRA1_NIVEL2, arrayJarras1.get(i));
                valuesPersonal.put(Utilidades.CAMPO_JARRA2_NIVEL2, arrayJarras2.get(i));
                valuesPersonal.put(Utilidades.CAMPO_FECHA_NIVEL2, BDFecha);
                valuesPersonal.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, BDhoraInicio);
                valuesPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, BDhoraFinal);
                valuesPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");

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

        ArrayList<String> arrayListPersonales = new ArrayList<>();
        String BDfundo = "";
        String BDmodulo = "";
        String BDlote = "";
        String BDlabor = "";
        String BDdniSupervisor = "";
        String BDjarra1 = "";
        String BDjarra2 = "";
        String BDFecha = "";
        String BDhoraInicio = "";
        String BDhoraFinal = "";
        String BDestado = "";

        if (cursorPersonal != null){
            if (cursorPersonal.moveToFirst()){
                do{
                    BDfundo = cursorPersonal.getString(2);
                    BDmodulo = cursorPersonal.getString(3);
                    BDlote = cursorPersonal.getString(4);
                    BDlabor = cursorPersonal.getString(5);
                    arrayListPersonales.add(cursorPersonal.getString(6));
                    BDdniSupervisor = cursorPersonal.getString(7);
                    BDjarra1 = cursorPersonal.getString(8);
                    BDjarra2 = cursorPersonal.getString(9);
                    BDFecha = cursorPersonal.getString(10);
                    BDhoraInicio = cursorPersonal.getString(11);
                    BDhoraFinal = cursorPersonal.getString(12);
                    BDestado = cursorPersonal.getString(13);
                }while (cursorPersonal.moveToNext());
            }
        }

        SQLiteDatabase databaseGrupo = conn.getWritableDatabase();
        Cursor cursorGrupo = databaseGrupo.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL1_5 + " WHERE " + Utilidades.CAMPO_DNI_NIVEL1_5 + "=" + "'"+dni+"'", null);

        String idNewGrupo = UUID.randomUUID().toString();

        if (cursorGrupo.getCount()<0){
            valuesAgregarGrupo.put(Utilidades.CAMPO_ID_GRUPO_NIVEL1_5, idNewGrupo);
            valuesAgregarGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_NIVEL1_5, 1);
            valuesAgregarGrupo.put(Utilidades.CAMPO_DNI_NIVEL1_5, BDdniSupervisor);
        }else{
            valuesAgregarGrupo.put(Utilidades.CAMPO_ID_GRUPO_NIVEL1_5, idNewGrupo);
            valuesAgregarGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_NIVEL1_5, cursorGrupo.getCount()+1);
            valuesAgregarGrupo.put(Utilidades.CAMPO_DNI_NIVEL1_5, BDdniSupervisor);
        }

        databaseGrupo.insert(Utilidades.TABLA_NIVEL1_5, Utilidades.CAMPO_DNI_NIVEL1_5, valuesAgregarGrupo);

        for (int i=0; i<rvListadoPersonal.getChildCount(); i++){
            SQLiteDatabase database = conn.getWritableDatabase();
            ContentValues valuesPersonalClonado = new ContentValues();

            valuesPersonalClonado.put(Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2, idNewGrupo);
            valuesPersonalClonado.put(Utilidades.CAMPO_FUNDO_NIVEL2, BDfundo);
            valuesPersonalClonado.put(Utilidades.CAMPO_MODULO_NIVEL2, BDmodulo);
            valuesPersonalClonado.put(Utilidades.CAMPO_LOTE_NIVEL2, BDlote);
            valuesPersonalClonado.put(Utilidades.CAMPO_LABOR_NIVEL2, BDlabor);
            valuesPersonalClonado.put(Utilidades.CAMPO_PERSONAL_NIVEL2, arrayListPersonales.get(i));
            valuesPersonalClonado.put(Utilidades.CAMPO_DNI_NIVEL2, BDdniSupervisor);
            valuesPersonalClonado.put(Utilidades.CAMPO_JARRA1_NIVEL2, BDjarra1);
            valuesPersonalClonado.put(Utilidades.CAMPO_JARRA2_NIVEL2, BDjarra2);
            valuesPersonalClonado.put(Utilidades.CAMPO_FECHA_NIVEL2, BDFecha);
            valuesPersonalClonado.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, BDhoraInicio);
            valuesPersonalClonado.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, BDhoraFinal);
            valuesPersonalClonado.put(Utilidades.CAMPO_ESTADO_NIVEL2, BDestado);

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

    private void iniciarScanJarra(){
        if (!valorPersonal.isEmpty()){
            if (contadorJarras == 2){
                personalTrabajoArrayList.add(new E_PersonalTrabajo(String.valueOf(contador++), valorPersonal, valorJarra1.concat(" - ").concat(valorJarra2)));
                //arrayInformacion.add(valorPersonal.concat(" - ").concat(valorJarra1).concat(" - ").concat(valorJarra2));
                iniciarScanPersonal();
                valorPersonal = "";
                valorJarra1 = "";
                valorJarra2 = "";//
                contadorJarras = 0;
            }else{
                IntentIntegrator integrator = new IntentIntegrator(TercerNivelConfiguracionGrupo.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("LECTOR QR JARRAS");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        }else{
            iniciarScanPersonal();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            if (intentResult.getContents() == null){

                if (valorPersonal.isEmpty() && valorJarra1.isEmpty() && valorJarra2.isEmpty()){
                    Toast.makeText(this, "Listando los registros", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Te faltaron llenar datos.", Toast.LENGTH_SHORT).show();
                    contadorJarras = 0;
                    valorPersonal = "";
                    valorJarra1 = "";
                    valorJarra2 = "";
                    iniciarScanPersonal();
                }
                adaptadorListarPersonalTrabajo = new AdaptadorListarPersonalTrabajo(this, personalTrabajoArrayList);
                lvRegistrarPersonal.setAdapter(adaptadorListarPersonalTrabajo);
            }else{
                if (valorPersonal.isEmpty()){
                    if (valorPersonal.contains(intentResult.getContents())){
                        Toast.makeText(this, "Personal Duplicado", Toast.LENGTH_SHORT).show();
                        iniciarScanJarra();
                    }else{
                        if (arrayPersonalList.contains(intentResult.getContents())){
                            Toast.makeText(this, "Ya se encuentra registrado", Toast.LENGTH_SHORT).show();
                            iniciarScanPersonal();
                        }else{
                            Toast.makeText(this, "Personal Registrado", Toast.LENGTH_SHORT).show();
                            arrayPersonal.add(intentResult.getContents());
                            valorPersonal = intentResult.getContents();
                            iniciarScanJarra();
                        }
                    }
                }else{
                    if (valorJarra1.isEmpty()){
                        if (valorJarra1.contains(intentResult.getContents()) || valorJarra2.contains(intentResult.getContents())){
                            Toast.makeText(this, "Jarra Duplicada", Toast.LENGTH_SHORT).show();
                            iniciarScanJarra();
                        }else{
                            if (intentResult.getContents().length()>4){
                                Toast.makeText(this, "Valor De Jarra Excedido!", Toast.LENGTH_SHORT).show();
                                iniciarScanJarra();
                            }else{
                                contadorJarras += 1;
                                Toast.makeText(this, "Jarra Registrada", Toast.LENGTH_SHORT).show();
                                arrayJarras1.add(intentResult.getContents());
                                valorJarra1 = intentResult.getContents();
                                iniciarScanJarra();
                            }
                        }
                    }else{
                        if (valorJarra1.contains(intentResult.getContents()) || valorJarra2.contains(intentResult.getContents())){
                            Toast.makeText(this, "Jarra Duplicada", Toast.LENGTH_SHORT).show();
                            iniciarScanJarra();
                        }else{
                            if (intentResult.getContents().length()>4){
                                Toast.makeText(this, "Valor De Jarra Excedido!", Toast.LENGTH_SHORT).show();
                                iniciarScanJarra();
                            }else{
                                contadorJarras += 1;
                                Toast.makeText(this, "Jarra Registrada", Toast.LENGTH_SHORT).show();
                                arrayJarras2.add(intentResult.getContents());
                                valorJarra2 = intentResult.getContents();
                                iniciarScanJarra();
                            }
                        }
                    }
                }
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