package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
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
import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.R;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListarPersonalTrabajo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class TercerNivelAgregarPersonal extends AppCompatActivity {

    ListView lvListadoPersonal, lvRegistrarPersonal;
    FloatingActionButton fbAgregarPersonal;
    Button btnEditarPersona, btnAgregarPersonal;

    ArrayList<String> arrayPersonalList;

    ConexionSQLiteHelper conn;

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

    ContentValues valuesEditarPersonal = new ContentValues();
    ContentValues valuesAgregarGrupo = new ContentValues();

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
        setContentView(R.layout.activity_tercer_nivel_asignar_jarras);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        lvListadoPersonal = findViewById(R.id.lvListadoPersonalACTTercerlNivelRRHH_TAREO_ARANDANO);
        lvRegistrarPersonal = findViewById(R.id.lvListadoPersonalRegTercerlNivelRRHH_TAREO_ARANDANO);
        fbAgregarPersonal = findViewById(R.id.fbAgregarPersonalNuevoRRHH_TAREO_AR);
        btnEditarPersona = findViewById(R.id.btnEditarPersonalNuevoRRHH_TAREO_AR);
        btnAgregarPersonal = findViewById(R.id.btnRegistrarPersonalNuevoRRHH_TAREO_AR);

        Bundle bundle = getIntent().getExtras();
        idGrupo = bundle.getString("idGrupo");
        dni = bundle.getString("dni");
        valor = bundle.getInt("valor");

        if (valor == 1){
            btnEditarPersona.setVisibility(View.VISIBLE);
        }
        if (valor == 2){

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

        consultarGruposTrabajo();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayPersonalList);
        lvListadoPersonal.setAdapter(adapter);

        lvListadoPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obtenerDatosPersonal(BDdniPersonal);
            }
        });

        fbAgregarPersonal.setOnClickListener(view->{
            lvListadoPersonal.setVisibility(View.GONE);
            lvRegistrarPersonal.setVisibility(View.VISIBLE);
            iniciarScanPersonal();
        });
        btnEditarPersona.setOnClickListener(view->registrarNuevoPersonal());
        //btnAgregarPersonal.setOnClickListener(view->registrarNuevoPersonal());
    }

    private void consultarGruposTrabajo(){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2 + "=" + "'"+idGrupo+"'", null);

        if (cursor != null){
            while (cursor.moveToNext()) {
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
            }
        }
    }

    private void obtenerDatosPersonal(String dni_personal){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_PERSONAL_NIVEL2 + "=" + "'"+dni_personal+"'", null);

        if (cursor != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = getLayoutInflater().inflate(R.layout.content_rrhh_tareo_arandano_especificacion_personal, null, false);
            EditText etHoraInicio = view.findViewById(R.id.etHoraInicioEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            EditText etHoraFinal = view.findViewById(R.id.etHoraFinalEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            RadioButton rbAbierto = view.findViewById(R.id.rbEstadoAbiertoEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            RadioButton rbCerrado = view.findViewById(R.id.rbEstadoCerradoEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);

            etHoraFinal.setOnClickListener(v ->{
                final Calendar c = Calendar.getInstance();
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etHoraFinal.setText(hourOfDay+":"+minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.show();
            });

            if (cursor.moveToFirst()){
                etHoraInicio.setText(cursor.getString(11));
                etHoraFinal.setText(cursor.getString(12));

                if (cursor.getString(13).equals("ABIERTO")){
                    rbAbierto.setChecked(true);
                }else{
                    rbCerrado.setChecked(true);
                }

            }
            builder.setView(view);

            builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (rbAbierto.isChecked()){
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");

                        String[] parametro = {BDid_nivel2};
                        database.update(Utilidades.TABLA_NIVEL2, valuesEditarPersonal, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);

                        Toast.makeText(TercerNivelAgregarPersonal.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                    }
                    if (rbCerrado.isChecked()){
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "CERRADO");

                        String[] parametro = {BDid_nivel2};
                        database.update(Utilidades.TABLA_NIVEL2, valuesEditarPersonal, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);

                        Toast.makeText(TercerNivelAgregarPersonal.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void registrarNuevoPersonal(){
        if (personalTrabajoArrayList.size()>0){
            SQLiteDatabase database = conn.getWritableDatabase();
            ContentValues valuesPersonal = new ContentValues();
            for (int i=0; i<lvListadoPersonal.getAdapter().getCount(); i++){
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

                    Intent intent = new Intent(TercerNivelAgregarPersonal.this, SegundoNivelWelcome.class);
                    startActivity(intent);
                }
            }
        }
    }

    private void duplicarGrupoTrabajo(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorPersonal = dataObtenida.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2 + "=" + "'"+idGrupo+"'", null);

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

        for (int i=0; i<lvListadoPersonal.getAdapter().getCount(); i++){
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
                Intent intent = new Intent(TercerNivelAgregarPersonal.this, SegundoNivelWelcome.class);
                startActivity(intent);
            }
        }
    }

    private void iniciarScanPersonal(){
        IntentIntegrator integrator = new IntentIntegrator(TercerNivelAgregarPersonal.this);
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
                personalTrabajoArrayList.add(new E_PersonalTrabajo(String.valueOf(contador++), valorPersonal, valorJarra1.concat(" - ").concat(valorJarra2), BDhoraInicio, BDhoraFinal));
                //arrayInformacion.add(valorPersonal.concat(" - ").concat(valorJarra1).concat(" - ").concat(valorJarra2));
                iniciarScanPersonal();
                valorPersonal = "";
                valorJarra1 = "";
                valorJarra2 = "";//
                contadorJarras = 0;
            }else{
                IntentIntegrator integrator = new IntentIntegrator(TercerNivelAgregarPersonal.this);
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
        Intent intent = new Intent(TercerNivelAgregarPersonal.this, SegundoNivelWelcome.class);
        startActivity(intent);
    }
}