package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.R;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
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

    ListView lvListadoPersonal;
    FloatingActionButton fbAgregarPersonal;
    Button btnAgregarPersonal;
    Spinner spModulo, spLote, spLabor;

    ArrayList<String> arrayPersonalList;
    ArrayList<String> arrayPersonalNuevoList = new ArrayList<>();
    ArrayList<String> arrayJarra1NuevoList = new ArrayList<>();
    ArrayList<String> arrayJarra2NuevoList = new ArrayList<>();
    ArrayList<String> arrayInformacion = new ArrayList<>();

    ConexionSQLiteHelper conn;


    ArrayList<String> arrayModulo = new ArrayList<>();
    ArrayList<String> arrayLote = new ArrayList<>();
    ArrayList<String> arrayLabor = new ArrayList<>();

    String fundo = "";
    String modulo = "";
    String lote = "";
    String labor = "";
    String idGrupo = "";
    String dni = "";

    int contadorJarras = 0;
    String valorPersonal = "";
    String valorJarra1 = "";
    String valorJarra2 = "";

    /*VALORES OBTENIDOS BD*/
    String BDgrupo = "";
    String BDfundo = "";
    String BDmodulo = "";
    String BDlote = "";
    String BDlabor = "";
    String BDdniSupervisor = "";

    ContentValues valuesGrupo = new ContentValues();
    ContentValues valuesPersonal = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_asignar_jarras);

        conn = new ConexionSQLiteHelper(getApplicationContext(), "athos0", null, 1);

        spModulo = findViewById(R.id.spModuloACTRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        spLote = findViewById(R.id.spLoteACTRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        spLabor = findViewById(R.id.spLaborACTRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        lvListadoPersonal = findViewById(R.id.lvListadoPersonalACTTercerlNivelRRHH_TAREO_ARANDANO);
        fbAgregarPersonal = findViewById(R.id.fbAgregarPersonalNuevoRRHH_TAREO_AR);
        btnAgregarPersonal = findViewById(R.id.btnRegistrarPersonalNuevoRRHH_TAREO_AR);

        Bundle bundle = getIntent().getExtras();
        idGrupo = bundle.getString("idGrupo");
        dni = bundle.getString("dni");

        consultarGruposTrabajo();

        recibirFundo();
        cargarModulo();
        cargarLabores();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayPersonalList);
        lvListadoPersonal.setAdapter(adapter);

        fbAgregarPersonal.setOnClickListener(view->iniciarScanPersonal());
        btnAgregarPersonal.setOnClickListener(view->registrarNuevoPersonal());

        spModulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                modulo = spModulo.getItemAtPosition(i).toString();

                arrayLote.clear();
                switch (modulo){
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

                ArrayAdapter<String> adaptadorLote = new ArrayAdapter<>(TercerNivelAgregarPersonal.this, android.R.layout.simple_spinner_dropdown_item, arrayLote);
                spLote.setAdapter(adaptadorLote);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spLote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lote = spLote.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spLabor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                labor = spLabor.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void cargarModulo(){
        ArrayAdapter<String> adaptadorModulo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrayModulo);
        spModulo.setAdapter(adaptadorModulo);
    }

    public void cargarLabores(){
        String[] labores = {"ARREGLO DE CABECERAS/SURCOS","ARREGLO DE PLANTAS"};
        ArrayAdapter<String> adaptadorLabores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labores);
        spLabor.setAdapter(adaptadorLabores);
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

    private void consultarGruposTrabajo(){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2 + "=" + "'"+idGrupo+"'", null);

        if (cursor != null){
            while (cursor.moveToNext()) {
                arrayPersonalList.add(cursor.getString(6));
                BDgrupo = cursor.getString(1);
                BDfundo = cursor.getString(2);
                BDmodulo = cursor.getString(3);
                BDlote = cursor.getString(4);
                BDlabor = cursor.getString(5);
                BDdniSupervisor = cursor.getString(7);
            }
        }
    }

    private void registrarNuevoPersonal(){
        if (arrayInformacion.size()==0){
            Toast.makeText(this, "No hay personal", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();

        for (int i=0; i<arrayPersonalNuevoList.size(); i++){
            valuesPersonal.put(Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2, BDgrupo);
            valuesPersonal.put(Utilidades.CAMPO_FUNDO_NIVEL2, BDfundo);
            valuesPersonal.put(Utilidades.CAMPO_MODULO_NIVEL2, BDmodulo);
            valuesPersonal.put(Utilidades.CAMPO_LOTE_NIVEL2, BDlote);
            valuesPersonal.put(Utilidades.CAMPO_LABOR_NIVEL2, BDlabor);
            valuesPersonal.put(Utilidades.CAMPO_PERSONAL_NIVEL2, arrayPersonalNuevoList.get(i));
            valuesPersonal.put(Utilidades.CAMPO_DNI_NIVEL2, BDdniSupervisor);
            valuesPersonal.put(Utilidades.CAMPO_JARRA1_NIVEL2, arrayJarra1NuevoList.get(i));
            valuesPersonal.put(Utilidades.CAMPO_JARRA2_NIVEL2, arrayJarra2NuevoList.get(i));
            valuesPersonal.put(Utilidades.CAMPO_FECHA_NIVEL2, obtenerFechaActual("AMERICA/Lima"));
            valuesPersonal.put(Utilidades.CAMPO_HORA_NIVEL2, obtenerHoraActual("AMERICA/Lima"));

            Long idResultante = database.insert(Utilidades.TABLA_NIVEL2, Utilidades.CAMPO_ID_NIVEL2, valuesPersonal);

            if (idResultante > 0){
                Toast.makeText(this, "Datos Registrados!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TercerNivelAgregarPersonal.this, SegundoNivelWelcome.class);
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
                arrayInformacion.add(valorPersonal.concat(" - ").concat(valorJarra1).concat(" - ").concat(valorJarra2));
                iniciarScanPersonal();
                valorPersonal = "";
                valorJarra1 = "";
                valorJarra2 = "";
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
                Toast.makeText(this, "Lectura Cancelada", Toast.LENGTH_SHORT).show();
                contadorJarras = 0;
                valorPersonal = "";
                valorJarra1 = "";
                valorJarra2 = "";

                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayInformacion);
                lvListadoPersonal.setAdapter(adapter);

            }else{
                Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show();

                if (valorPersonal.isEmpty()){
                    arrayPersonalNuevoList.add(intentResult.getContents());
                    valorPersonal = intentResult.getContents();
                    iniciarScanJarra();
                }else{
                    contadorJarras += 1;

                    if (valorJarra1.isEmpty()){
                        arrayJarra1NuevoList.add(intentResult.getContents());
                        valorJarra1 = intentResult.getContents();
                        iniciarScanJarra();
                    }else{
                        arrayJarra2NuevoList.add(intentResult.getContents());
                        valorJarra2 = intentResult.getContents();
                        iniciarScanJarra();
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