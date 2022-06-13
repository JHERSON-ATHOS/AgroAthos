package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.MainActivity;
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

public class SegundoNivelRegistrarGrupoTrabajo extends AppCompatActivity {

    Spinner spModulo, spLote, spLabor;
    FloatingActionButton fbRegistrarPersonal;
    Button btnRegistrarPersonal, btnHoraInicio, btnHoraFinal;
    ListView lvPersonal;
    TextView tvHoraInicio, tvHoraFinal;

    ArrayList<String> arrayPersonal = new ArrayList<>();
    ArrayList<String> arrayJarras1 = new ArrayList<>();
    ArrayList<String> arrayJarras2 = new ArrayList<>();
    ArrayList<String> arrayModulo = new ArrayList<>();
    ArrayList<String> arrayLote = new ArrayList<>();

    ArrayList<E_PersonalTrabajo> personalTrabajoArrayList = new ArrayList<>();
    AdaptadorListarPersonalTrabajo adaptadorListarPersonalTrabajo;
    int contador = 1;

    String zona = "";
    String fundo = "";
    String modulo = "";
    String lote = "";
    String labor = "";
    String dni = "";
    String idGrupo = "";

    int contadorJarras = 0;
    String valorPersonal = "";
    String valorJarra1 = "";
    String valorJarra2 = "";

    ConexionSQLiteHelper conn;
    ContentValues valuesGrupo = new ContentValues();
    ContentValues valuesPersonal = new ContentValues();

    private int hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_nivel_registrar_grupo_trabajo);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        spModulo = findViewById(R.id.spModuloRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        spLote = findViewById(R.id.spLoteRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        spLabor = findViewById(R.id.spLaborRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        btnHoraInicio = findViewById(R.id.btnHoraInicioRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        btnHoraFinal = findViewById(R.id.btnHoraFinalRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        tvHoraInicio = findViewById(R.id.tvHoraInicioRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        tvHoraFinal = findViewById(R.id.tvHoraFinalRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        lvPersonal = findViewById(R.id.lvPersonalRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        btnRegistrarPersonal = findViewById(R.id.btnRegistrarPersonalRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);
        fbRegistrarPersonal = findViewById(R.id.fbRegistrarPersonalRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL_REGISTRAR_GRUPO_TRABAJO);

        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        dni = preferences.getString("dni","");
        fundo = preferences.getString("fundo","");
        zona = preferences.getString("zona","");

        recibirFundo();
        cargarModulo();
        cargarLabores();

        fbRegistrarPersonal.setOnClickListener(view -> {
            if (tvHoraInicio.getText().toString().isEmpty() || tvHoraFinal.getText().toString().isEmpty()){
                Toast.makeText(this, "Selecciona un rango de horas", Toast.LENGTH_SHORT).show();
            }else{
                iniciarScanPersonal();
            }
        } );

        btnRegistrarPersonal.setOnClickListener(view -> registrarPersonal() );

        btnHoraInicio.setOnClickListener(view ->{
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tvHoraInicio.setText(hourOfDay+":"+minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        });
        btnHoraFinal.setOnClickListener(view ->{
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    tvHoraFinal.setText(hourOfDay+":"+minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();
        });

        spModulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arrayLote.clear();
                modulo = spModulo.getItemAtPosition(i).toString();

                switch (modulo){
                    case"MO1":
                        switch (fundo){
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
                        switch (fundo){
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
                        switch (fundo){
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
                        if (fundo.equals("POM")){
                            arrayLote.add("L01");
                            arrayLote.add("L02");
                            arrayLote.add("L03");
                            arrayLote.add("L04");
                        }
                        break;
                    case "MO5":
                        if (fundo.equals("POM")){
                            arrayLote.add("L01");
                            arrayLote.add("L02");
                            arrayLote.add("L03");
                            arrayLote.add("L04");
                            arrayLote.add("L05");
                        }
                        break;
                }

                ArrayAdapter<String> adaptadorLote = new ArrayAdapter<>(SegundoNivelRegistrarGrupoTrabajo.this, android.R.layout.simple_spinner_dropdown_item, arrayLote);
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
        String[] labores = {"ARREGLO DE CABECERAS/SURCOS","ARREGLO DE PLANTAS","COSECHA"};
        ArrayAdapter<String> adaptadorLabores = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, labores);
        spLabor.setAdapter(adaptadorLabores);
    }

    public void recibirFundo(){
        switch (fundo){
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

    private void registrarPersonal(){
        if (lvPersonal.getCount()==0){
            Toast.makeText(this, "No hay personal", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL1_5 + " WHERE " + Utilidades.CAMPO_DNI_NIVEL1_5 + "=" + "'"+dni+"'", null);

        idGrupo = UUID.randomUUID().toString();

        if (cursor.getCount()<0){
            valuesGrupo.put(Utilidades.CAMPO_ID_GRUPO_NIVEL1_5, idGrupo);
            valuesGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_NIVEL1_5, 1);
            valuesGrupo.put(Utilidades.CAMPO_DNI_NIVEL1_5, dni);
        }else{
            valuesGrupo.put(Utilidades.CAMPO_ID_GRUPO_NIVEL1_5, idGrupo);
            valuesGrupo.put(Utilidades.CAMPO_CONTADOR_GRUPO_NIVEL1_5, cursor.getCount()+1);
            valuesGrupo.put(Utilidades.CAMPO_DNI_NIVEL1_5, dni);
        }

        database.insert(Utilidades.TABLA_NIVEL1_5, Utilidades.CAMPO_DNI_NIVEL1_5, valuesGrupo);

        for (int i=0; i<lvPersonal.getAdapter().getCount(); i++){
            valuesPersonal.put(Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2, idGrupo);
            valuesPersonal.put(Utilidades.CAMPO_FUNDO_NIVEL2, fundo);
            valuesPersonal.put(Utilidades.CAMPO_MODULO_NIVEL2, modulo);
            valuesPersonal.put(Utilidades.CAMPO_LOTE_NIVEL2, lote);
            valuesPersonal.put(Utilidades.CAMPO_LABOR_NIVEL2, labor);
            valuesPersonal.put(Utilidades.CAMPO_PERSONAL_NIVEL2, arrayPersonal.get(i));
            valuesPersonal.put(Utilidades.CAMPO_DNI_NIVEL2, dni);
            valuesPersonal.put(Utilidades.CAMPO_JARRA1_NIVEL2, arrayJarras1.get(i));
            valuesPersonal.put(Utilidades.CAMPO_JARRA2_NIVEL2, arrayJarras2.get(i));
            valuesPersonal.put(Utilidades.CAMPO_FECHA_NIVEL2, obtenerFechaActual("AMERICA/Lima"));
            valuesPersonal.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, tvHoraInicio.getText().toString());
            valuesPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, tvHoraFinal.getText().toString());
            valuesPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");

            Long idResultante = database.insert(Utilidades.TABLA_NIVEL2, Utilidades.CAMPO_ID_NIVEL2, valuesPersonal);

            if (idResultante > 0){
                Toast.makeText(this, "Datos Registrados!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SegundoNivelRegistrarGrupoTrabajo.this, SegundoNivelWelcome.class);
                startActivity(intent);
            }
        }
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
        IntentIntegrator integrator = new IntentIntegrator(SegundoNivelRegistrarGrupoTrabajo.this);
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
                IntentIntegrator integrator = new IntentIntegrator(SegundoNivelRegistrarGrupoTrabajo.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("ERROR");
                    builder.setMessage("Te faltaron llenar datos.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            contadorJarras = 0;
                            valorPersonal = "";
                            valorJarra1 = "";
                            valorJarra2 = "";
                            arrayPersonal.clear();
                            iniciarScanPersonal();
                        }
                    });
                    builder.create().show();
                }
                adaptadorListarPersonalTrabajo = new AdaptadorListarPersonalTrabajo(this, personalTrabajoArrayList);
                lvPersonal.setAdapter(adaptadorListarPersonalTrabajo);
            }else{
                if (valorPersonal.isEmpty()){
                    if (valorPersonal.contains(intentResult.getContents())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("ERROR");
                        builder.setMessage("El personal ya está registrado.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                iniciarScanJarra();
                            }
                        });
                        builder.create().show();
                    }else{
                        if (arrayPersonal.contains(intentResult.getContents())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("ERROR");
                            builder.setMessage("El personal ya se encuentra en la lista.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    iniciarScanJarra();
                                }
                            });
                            builder.create().show();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("ERROR");
                            builder.setMessage("La jarra ya se encuentra registrada.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    iniciarScanJarra();
                                }
                            });
                            builder.create().show();
                        }else{
                            if (intentResult.getContents().length()>4){
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("ERROR");
                                builder.setMessage("El límite de jarra es 9999.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        iniciarScanJarra();
                                    }
                                });
                                builder.create().show();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("ERROR");
                            builder.setMessage("La jarra ya se encuentra registrada.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    iniciarScanJarra();
                                }
                            });
                            builder.create().show();
                        }else{
                            if (intentResult.getContents().length()>4){
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setTitle("ERROR");
                                builder.setMessage("El límite de jarra es 9999.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        iniciarScanJarra();
                                    }
                                });
                                builder.create().show();
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
        Intent intent = new Intent(SegundoNivelRegistrarGrupoTrabajo.this, SegundoNivelWelcome.class);
        startActivity(intent);
    }
}