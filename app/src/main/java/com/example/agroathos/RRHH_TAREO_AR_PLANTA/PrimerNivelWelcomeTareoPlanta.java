package com.example.agroathos.RRHH_TAREO_AR_PLANTA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_Cultivos;
import com.example.agroathos.ENTIDADES.E_Fundos;
import com.example.agroathos.ENTIDADES.E_Lineas;
import com.example.agroathos.ENTIDADES.E_Naves;
import com.example.agroathos.ENTIDADES.E_Turnos;
import com.example.agroathos.ENTIDADES.E_Zonas;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorCultivos;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorFundos;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorZonas;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorLineas;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorNaves;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorTurnos;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class PrimerNivelWelcomeTareoPlanta extends AppCompatActivity {

    Spinner spNave, spLinea, spTurno;
    Button btnRegistrar;
    ConstraintLayout layout;

    ArrayList<E_Naves> navesArrayList = new ArrayList<>();
    ArrayList<E_Lineas> lineasArrayList = new ArrayList<>();
    ArrayList<E_Turnos> turnosArrayList = new ArrayList<>();

    AdaptadorNaves adaptadorNaves;
    AdaptadorLineas adaptadorLineas;
    AdaptadorTurnos adaptadorTurnos;

    String dniSupervisor = "";
    String nave = "";
    String linea = "";
    String turno = "";

    ConexionSQLiteHelper conn;
    SharedPreferences preferences;

    String id_nivel_uno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome_tareo_planta);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);
        preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);
        layout = findViewById(R.id.clPrincipalRRHH_TAREO_AR_PLANTA);

        dniSupervisor = preferences.getString("dni", "");

        spNave = findViewById(R.id.spNaveRRHH_TAREO_ARANDANO_PLANTA_PRIMER_NIVEL);
        spLinea = findViewById(R.id.spLineaRRHH_TAREO_ARANDANO_PLANTA_PRIMER_NIVEL);
        spTurno = findViewById(R.id.spTurnoRRHH_TAREO_ARANDANO_PLANTA_PRIMER_NIVEL);
        btnRegistrar = findViewById(R.id.btnIngresarRRHH_TAREO_ARANDANO_PLANTA_PRIMER_NIVEL);

        cargarDatos();

        spNave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Naves e_naves = navesArrayList.get(position);
                nave = e_naves.getNombre();

                switch (nave){
                    case "NPF01 - ICA":
                        lineasArrayList.clear();
                        lineasArrayList.add(new E_Lineas("-- Selecciona una Nave --"));
                        lineasArrayList.add(new E_Lineas("FGR01 - GRANADA"));
                        lineasArrayList.add(new E_Lineas("FAR01 - ARANDANO"));
                        lineasArrayList.add(new E_Lineas("IND - INDIRECTO"));
                        break;
                    case "NPF02 - GUADALUPE":
                        lineasArrayList.clear();
                        lineasArrayList.add(new E_Lineas("-- Selecciona una Nave --"));
                        lineasArrayList.add(new E_Lineas("FEV02 - ESPARRAGO"));
                        lineasArrayList.add(new E_Lineas("FDB02 - DATIL"));
                        lineasArrayList.add(new E_Lineas("FHG02 - HIGO"));
                        lineasArrayList.add(new E_Lineas("IND - INDIRECTO"));
                        break;
                    case "NPF05 - NEPEÑA":
                        lineasArrayList.clear();
                        lineasArrayList.add(new E_Lineas("-- Selecciona una Nave --"));
                        lineasArrayList.add(new E_Lineas("FHF05 - HIGO"));
                        lineasArrayList.add(new E_Lineas("FAR05 - ARANDANO"));
                        lineasArrayList.add(new E_Lineas("IND - INDIRECTO"));
                        break;
                    case "NPF07 - CARAZ":
                        lineasArrayList.clear();
                        lineasArrayList.add(new E_Lineas("-- Selecciona una Nave --"));
                        lineasArrayList.add(new E_Lineas("FAR07 - ARANDANO"));
                        lineasArrayList.add(new E_Lineas("IND - INDIRECTO"));
                        break;
                }

                adaptadorLineas = new AdaptadorLineas(PrimerNivelWelcomeTareoPlanta.this, lineasArrayList);
                spLinea.setAdapter(adaptadorLineas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLinea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Lineas e_lineas = lineasArrayList.get(position);
                linea = e_lineas.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Turnos e_turnos = turnosArrayList.get(position);
                turno = e_turnos.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!preferences.getString("dniSupervisor", "").isEmpty()){
            Intent intent = new Intent(PrimerNivelWelcomeTareoPlanta.this, SegundoNivelWelcomeTareoPlanta.class);
            startActivity(intent);
        }

        btnRegistrar.setOnClickListener(view -> consultarDatos(dniSupervisor, nave, linea, turno));
    }

    private void cargarDatos(){
        navesArrayList.add(new E_Naves("-- Selecciona una Nave --"));
        lineasArrayList.add(new E_Lineas( "-- Selecciona una Linea --"));
        turnosArrayList.add(new E_Turnos( "-- Selecciona un Turno --"));

        navesArrayList.add(new E_Naves( "NPF01 - ICA"));
        navesArrayList.add(new E_Naves( "NPF02 - GUADALUPE"));
        navesArrayList.add(new E_Naves( "NPF05 - NEPEÑA"));
        navesArrayList.add(new E_Naves( "NPF07 - CARAZ"));

        turnosArrayList.add(new E_Turnos("DIA"));
        turnosArrayList.add(new E_Turnos("NOCHE"));

        adaptadorNaves = new AdaptadorNaves(PrimerNivelWelcomeTareoPlanta.this, navesArrayList);
        adaptadorLineas = new AdaptadorLineas(PrimerNivelWelcomeTareoPlanta.this, lineasArrayList);
        adaptadorTurnos = new AdaptadorTurnos(PrimerNivelWelcomeTareoPlanta.this, turnosArrayList);

        spNave.setAdapter(adaptadorNaves);
        spLinea.setAdapter(adaptadorLineas);
        spTurno.setAdapter(adaptadorTurnos);
    }

    private void enviarDatos(){

        if (nave.equals("-- Selecciona una Nave --")){
            Snackbar.make(layout, "Selecciona una Nave", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (linea.equals("-- Selecciona una Linea --")){
            Snackbar.make(layout, "Selecciona una Linea", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (turno.equals("-- Selecciona un Turno --")){
            Snackbar.make(layout, "Selecciona un Turno", Snackbar.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();

        id_nivel_uno = UUID.randomUUID().toString();

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_IDNIVEL1_TAREO_PLANTA_NIVEL1, id_nivel_uno);
        values.put(Utilidades.CAMPO_NAVE_TAREO_PLANTA_NIVEL1, nave);
        values.put(Utilidades.CAMPO_LINEA_TAREO_PLANTA_NIVEL1, linea);
        values.put(Utilidades.CAMPO_TURNO_TAREO_PLANTA_NIVEL1, turno);
        values.put(Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL1, dniSupervisor);
        values.put(Utilidades.CAMPO_FECHA_TAREO_PLANTA_NIVEL1, obtenerFechaActual("AMERICA/Lima"));
        values.put(Utilidades.CAMPO_HORA_TAREO_PLANTA_NIVEL1, obtenerHoraActual("GMT-5"));
        values.put(Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL1, "0");

        Long idResultante = database.insert(Utilidades.TABLA_TAREO_PLANTA_NIVEL1, Utilidades.CAMPO_ID_TAREO_PLANTA_NIVEL1, values);

        if (idResultante > 0){
            Toast.makeText(this, "Registrado!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PrimerNivelWelcomeTareoPlanta.this, SegundoNivelWelcomeTareoPlanta.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("dniSupervisor", dniSupervisor);
            editor.putString("idNivel1", id_nivel_uno);
            editor.putString("nave", nave);
            editor.putString("linea", linea);
            editor.putString("turno", turno);
            editor.commit();

            database.close();
            startActivity(intent);
        }
    }

    public void consultarDatos(String dni, String nave, String linea, String turno){
        SQLiteDatabase database = conn.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL1+" WHERE "
                +Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL1+"=" +"'"+dni+"' AND "+Utilidades.CAMPO_NAVE_TAREO_PLANTA_NIVEL1+"="+ "'"
                +nave+"' AND "+Utilidades.CAMPO_LINEA_TAREO_PLANTA_NIVEL1+"=" +"'"+linea+"' AND "+Utilidades.CAMPO_TURNO_TAREO_PLANTA_NIVEL1+"="
                +"'"+turno+"'", null);

        if (cursor != null){
            if (cursor.moveToNext()){
                String idNivel1BD = cursor.getString(1);
                String naveBD = cursor.getString(2);
                String lineaBD = cursor.getString(3);
                String turnoBD = cursor.getString(4);
                String dniBD = cursor.getString(5);

                Intent intent = new Intent(PrimerNivelWelcomeTareoPlanta.this, SegundoNivelWelcomeTareoPlanta.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("dni", dniBD);
                editor.putString("idNivel1", idNivel1BD);
                editor.putString("nave", naveBD);
                editor.putString("linea", lineaBD);
                editor.putString("turno", turnoBD);
                editor.commit();

                cursor.close();
                startActivity(intent);
            }else{
                enviarDatos();
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
    public void onBackPressed() {
        Intent intent = new Intent(PrimerNivelWelcomeTareoPlanta.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}